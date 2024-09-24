package com.example.resourcereservation.controller;

import com.example.resourcereservation.model.User;
import com.example.resourcereservation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Listar usuários", description = "Retorna uma lista de todos os usuários cadastrados.")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers()); // Para exibir todos os usuários
        return "users"; // Página que lista todos os usuários
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter usuário por ID", description = "Retorna os detalhes de um usuário específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "302", description = "Usuário não encontrado, redireciona para a lista de usuários")
    })
    public String getUser(@Parameter(description = "ID do usuário") @PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        if (user == null) {
            return "redirect:/users"; // Redireciona se o usuário não for encontrado
        }
        model.addAttribute("user", user);
        return "user-details"; // Página que mostra detalhes do usuário
    }

    @PostMapping
    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário com os dados fornecidos.")
    public String createUser(@Parameter(description = "Dados do usuário") @ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/users"; // Redireciona para a lista de usuários
    }

    @PostMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente.")
    public String updateUser(@Parameter(description = "ID do usuário") @PathVariable Long id,
                             @Parameter(description = "Dados atualizados do usuário") @ModelAttribute User user) {
        if (userService.findUserById(id) == null) {
            return "redirect:/users"; // Redireciona se o usuário não existir
        }
        user.setId(id);
        userService.saveUser(user);
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    @Operation(summary = "Excluir usuário", description = "Exclui um usuário existente.")
    public String deleteUser(@Parameter(description = "ID do usuário a ser excluído") @PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users"; // Redireciona para a lista de usuários
    }
}
