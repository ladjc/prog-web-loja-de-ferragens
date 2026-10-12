package com.ads.lojinha;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@CrossOrigin(origins = "*")
public class ProdutoController {

    @GetMapping("api/produto/{codigo}")
    public Produto retornaProduto(@PathVariable("codigo") int id) {

        // TODO buscar do DB

        var obj = new Produto();
        obj.setCodigo(id);
        obj.setNome("Caixa de Ferramentas 17 Polegadas");
        obj.setDescritivo("Caixa organizadora resistente com bandeja interna para ferramentas e acessórios.");
        obj.setQuantidade(30);
        obj.setValor(89.90);
        obj.setPromo(0.0);
        obj.setDestaque(1);

        return obj;

    }

    @PostMapping ("api/produto")
    public String gravar(@RequestBody Produto obj)
    {

        //TODO gravaar no banco de dados
        
        return "Produto gravado com sucesso:" + obj.toString();
    }
}
