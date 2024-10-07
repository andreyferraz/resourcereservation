package com.example.resourcereservation.controller;

import com.example.resourcereservation.model.Resource;
import com.example.resourcereservation.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/resources")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @GetMapping
    @Operation(summary = "Listar recursos", description = "Retorna uma lista de todos os recursos disponíveis.")
    public String listResources(Model model) {
        model.addAttribute("resources", resourceService.getAllResources());
        return "resources"; // Página que lista todos os recursos
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter recurso por ID", description = "Retorna os detalhes de um recurso específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recurso encontrado"),
        @ApiResponse(responseCode = "302", description = "Recurso não encontrado, redireciona para a lista de recursos")
    })
    public String getResource(@Parameter(description = "ID do recurso") @PathVariable Long id, Model model) {
        Resource resource = resourceService.getResourceById(id);
        if (resource == null) {
            return "redirect:/resources"; // Redireciona caso o recurso não seja encontrado
        }
        model.addAttribute("resource", resource);
        return "resource-details"; // Página que mostra detalhes do recurso
    }

    @PostMapping
    @Operation(summary = "Criar novo recurso", description = "Cria um novo recurso com os dados fornecidos.")
    public String createResource(@Parameter(description = "Dados do recurso") @ModelAttribute Resource resource) {
        resourceService.saveResource(resource);
        return "redirect:/resources"; // Redireciona para a lista de recursos
    }

    @PostMapping("/{id}")
    @Operation(summary = "Atualizar recurso", description = "Atualiza os dados de um recurso existente.")
    public String updateResource(@Parameter(description = "ID do recurso") @PathVariable Long id,
                                 @Parameter(description = "Dados atualizados do recurso") @ModelAttribute Resource resource) {
        resource.setId(id);
        resourceService.saveResource(resource);
        return "redirect:/resources";
    }

    @PostMapping("/{id}/delete")
    @Operation(summary = "Excluir recurso", description = "Exclui um recurso existente.")
    public String deleteResource(@Parameter(description = "ID do recurso a ser excluído") @PathVariable Long id) {
        resourceService.deleteResource(id);
        return "redirect:/resources";
    }
}
