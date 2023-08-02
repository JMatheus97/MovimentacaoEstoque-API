package com.project.server.services;

import com.project.server.exception.BadRequestException;
import com.project.server.exception.NotFoundException;
import com.project.server.model.MovimentacaoEstoque;
import com.project.server.model.Produto;
import com.project.server.model.enums.EnumTipoMovimentacao;
import com.project.server.repository.MovimentecaoEstoqueRepository;
import com.project.server.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private MovimentecaoEstoqueRepository movimentecaoEstoqueRepository;


    public Produto create(Produto produto){
        if(produto.getNome() == null){
            throw new BadRequestException("O nome do Produto não pode ser nulo.", null);
        }else {
            Produto produtoCodigo = produtoRepository.findByCodigoDeBarra(produto.getCodigoDeBarra());

            if(produtoCodigo != null){
                throw new BadRequestException("O codigo de barra já existe !", null);
            }
        }
        if(produto.getSaldoInicial() > 0){
            Produto newProduto = produtoRepository.save(produto);
            MovimentacaoEstoque movimentacaoEstoque = new MovimentacaoEstoque();
            movimentacaoEstoque.setProduto(newProduto);
            movimentacaoEstoque.setData(new Date());
            movimentacaoEstoque.setTipoMovimentacao(EnumTipoMovimentacao.SALDO_INICIAL.name());
            movimentacaoEstoque.setQuantidade(newProduto.getQuantidadeMinima());

            movimentecaoEstoqueRepository.save(movimentacaoEstoque);
            return newProduto;
        }else {
            return produtoRepository.save(produto);
        }

    }

    public Produto save(Produto produto){
        return produtoRepository.save(produto);
    }

    public List<Produto> findAll(){
        return produtoRepository.findAll();
    }

    public Produto findById(Long id){
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado."));
    }

    public void delete(Long id){
        produtoRepository.delete(produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado.")));
    }

    public Produto edit(Produto produtoNew) throws IllegalAccessException {
        Produto produtoOld = produtoRepository.findById(produtoNew.getId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado."));
        for (Field field : produtoNew.getClass().getDeclaredFields()){
            field.setAccessible(true);
            if (field.get(produtoNew) != null && !field.get(produtoNew).equals(field.get(produtoOld))){
                field.set(produtoOld, field.get(produtoNew));
            }
        }
        return produtoRepository.save(produtoOld);
    }


}
