package com.project.server.services;

import com.project.server.exception.BadRequestException;
import com.project.server.exception.NotFoundException;
import com.project.server.model.MovimentacaoEstoque;
import com.project.server.model.enums.EnumTipoMovimentacao;
import com.project.server.repository.MovimentecaoEstoqueRepository;
import com.project.server.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class MovimentacaoEstoqueService {

    @Autowired
    private MovimentecaoEstoqueRepository movimentecaoEstoqueRepository;
    @Autowired
    private ProdutoRepository produtoRepository;


    public MovimentacaoEstoque create(MovimentacaoEstoque movimentacaoEstoque){
        if(movimentacaoEstoque.getProduto() == null){
            throw new BadRequestException("O nome do Produto não pode ser nulo.", null);
        } else {
            List<MovimentacaoEstoque> movimentacaoEstoques = movimentecaoEstoqueRepository.findByMovimentcaoAndProduto(movimentacaoEstoque.getProduto().getId());
            if(movimentacaoEstoque.getTipoMovimentacao().equals(EnumTipoMovimentacao.SALDO_INICIAL.name())){
                if(movimentacaoEstoques != null && movimentacaoEstoques.size() > 0){
                    boolean hasSaldoInicial = movimentacaoEstoques.stream()
                            .anyMatch(mv -> mv.getTipoMovimentacao() == EnumTipoMovimentacao.SALDO_INICIAL.name());

                    if (hasSaldoInicial) {
                        throw new BadRequestException("O produto já possui movimentação SALDO INICIAL.", null);
                    }

                   if(movimentacaoEstoques.size() > 0){
                       throw new BadRequestException("O produto já possui movimentações SALDO INICIAL \n " +
                               "Não é possível criar movimentação SALDO INICIAL.", null);
                   }
                    return null;
                }else {
                    movimentecaoEstoqueRepository.save(movimentacaoEstoque);
                }
            }

            if(movimentacaoEstoque.getTipoMovimentacao().equals(EnumTipoMovimentacao.AJUSTE_SAÍDA.name())
                    || movimentacaoEstoque.getTipoMovimentacao().equals(EnumTipoMovimentacao.AJUSTE_ENTRADA.name())){
                if(movimentacaoEstoques == null){
                    throw new BadRequestException("Não é possível realizar lançamento de AJUSTE, pois não há movimentação de estoque.", null);
                }
            }
        }


        return movimentecaoEstoqueRepository.save(movimentacaoEstoque);
    }

    public MovimentacaoEstoque findLastByProdutoId(Long produtoId) {
        return movimentecaoEstoqueRepository.findLastByProdutoId(produtoId);
    }

    public MovimentacaoEstoque save(MovimentacaoEstoque movimentacaoEstoque){
        return movimentecaoEstoqueRepository.save(movimentacaoEstoque);
    }

    public List<MovimentacaoEstoque> findAll(){
        return movimentecaoEstoqueRepository.findAll();
    }


    public MovimentacaoEstoque findById(Long id){
        return movimentecaoEstoqueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimentação não encontrada."));
    }

    public void delete(Long id){
        movimentecaoEstoqueRepository.delete(movimentecaoEstoqueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimentação não encontrada.")));
    }

    public MovimentacaoEstoque edit(MovimentacaoEstoque movimentacaoEstoqueNew) throws IllegalAccessException {
        MovimentacaoEstoque movimentacaoEstoqueOld = movimentecaoEstoqueRepository.findById(movimentacaoEstoqueNew.getId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado."));
        for (Field field : movimentacaoEstoqueNew.getClass().getDeclaredFields()){
            field.setAccessible(true);
            if (field.get(movimentacaoEstoqueNew) != null && !field.get(movimentacaoEstoqueNew).equals(field.get(movimentacaoEstoqueOld))){
                field.set(movimentacaoEstoqueOld, field.get(movimentacaoEstoqueNew));
            }
        }
        return movimentecaoEstoqueRepository.save(movimentacaoEstoqueOld);
    }


}
