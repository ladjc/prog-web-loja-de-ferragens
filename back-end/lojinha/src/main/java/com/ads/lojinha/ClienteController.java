package com.ads.lojinha;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ClienteController {

    // Lista apenas para testar os métodos
    private final List<Cliente> clientes = new ArrayList<>();
    // Pula o id 1
    private long proximoId = 2;

    public ClienteController() {
        // Registro padrão
        clientes.add(new Cliente(
                1L,
                "Leonel",
                "leonel@email.com",
                "11-12345678",
                "987654321",
                "asichb@aASIH",
                "Rua do Sol, 123"));
    }

    @GetMapping("api/cliente")
    public ResponseEntity<List<Cliente>> listar() {

        List<Cliente> resposta = new ArrayList<>();
        for (Cliente c : clientes) {
            resposta.add(semSenha(c));
        }

        return ResponseEntity.ok(resposta); // 200
    }

    // GET api/cliente/{id}  ->  200 OK | 400 Bad Request | 404 Not Found
    @GetMapping("api/cliente/{id}")
    public ResponseEntity<?> buscar(@PathVariable("id") long id) {

        if (id <= 0) {
            return ResponseEntity.badRequest().body("Id inválido"); // 400
        }

        Cliente encontrado = buscarPorId(id);
        if (encontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado"); // 404
        }

        return ResponseEntity.ok(semSenha(encontrado)); // 200
    }

    // POST api/cliente  ->  201 Created | 400 Bad Request | 409 Conflict
    @PostMapping("api/cliente")
    public ResponseEntity<?> gravar(@RequestBody Cliente obj) {

        String erro = validar(obj);
        if (erro != null) {
            return ResponseEntity.badRequest().body(erro); // 400
        }

        String conflito = verificarDuplicado(obj, 0);
        if (conflito != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflito); // 409
        }

        obj.setId(proximoId++); // ignora o id que veio no JSON e gera um novo
        clientes.add(obj);

        // 201 + cabeçalho Location apontando para o novo recurso
        return ResponseEntity.created(URI.create("/api/cliente/" + obj.getId())).body(semSenha(obj));
    }

    // PUT api/cliente/{id}  ->  200 OK | 400 Bad Request | 404 Not Found | 409 Conflict
    @PutMapping("api/cliente/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") long id, @RequestBody Cliente obj) {

        if (id <= 0) {
            return ResponseEntity.badRequest().body("Id inválido"); // 400
        }

        String erro = validar(obj);
        if (erro != null) {
            return ResponseEntity.badRequest().body(erro); // 400
        }

        Cliente existente = buscarPorId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado"); // 404
        }

        // o próprio cliente não conta como duplicado dele mesmo
        String conflito = verificarDuplicado(obj, id);
        if (conflito != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflito); // 409
        }

        existente.setNome(obj.getNome());
        existente.setEmail(obj.getEmail());
        existente.setTelefone(obj.getTelefone());
        existente.setDocumento(obj.getDocumento());
        existente.setSenha(obj.getSenha());
        existente.setEndereco(obj.getEndereco());

        return ResponseEntity.ok(semSenha(existente)); // 200
    }

    // DELETE api/cliente/{id}  ->  204 No Content | 400 Bad Request | 404 Not Found
    @DeleteMapping("api/cliente/{id}")
    public ResponseEntity<?> remover(@PathVariable("id") long id) {

        if (id <= 0) {
            return ResponseEntity.badRequest().body("Id inválido"); // 400
        }

        Cliente existente = buscarPorId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado"); // 404
        }

        clientes.remove(existente);

        return ResponseEntity.noContent().build(); // 204 (sem corpo)
    }


    // Procura o cliente na lista pelo id (devolve null se não achar)
    private Cliente buscarPorId(long id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    // Verifica se já existe outro cliente com o mesmo e-mail ou documento.
    // ignorarId = id do cliente que está sendo atualizado (use 0 no cadastro).
    // Devolve a mensagem de erro, ou null se não há duplicado.
    private String verificarDuplicado(Cliente novo, long ignorarId) {
        for (Cliente c : clientes) {
            if (c.getId() == ignorarId) {
                continue;
            }
            if (c.getEmail().equalsIgnoreCase(novo.getEmail())) {
                return "E-mail já cadastrado";
            }
            if (c.getDocumento().equals(novo.getDocumento())) {
                return "Documento já cadastrado";
            }
        }
        return null;
    }

    // Cópia do cliente sem a senha: a senha nunca é devolvida nas respostas
    private Cliente semSenha(Cliente c) {
        return new Cliente(
                c.getId(),
                c.getNome(),
                c.getEmail(),
                c.getTelefone(),
                c.getDocumento(),
                null,
                c.getEndereco());
    }

    // Devolve a mensagem de erro, ou null se estiver tudo certo
    private String validar(Cliente c) {
        if (vazio(c.getNome())) {
            return "Nome é obrigatório";
        }
        if (vazio(c.getEmail()) || !c.getEmail().contains("@")) {
            return "E-mail inválido";
        }
        if (vazio(c.getDocumento())) {
            return "Documento (CPF) é obrigatório";
        }
        if (vazio(c.getSenha())) {
            return "Senha é obrigatória";
        }
        return null;
    }

    private boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }
}