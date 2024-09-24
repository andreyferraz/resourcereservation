package com.example.resourcereservation.controller;

import com.example.resourcereservation.model.Reservation;
import com.example.resourcereservation.service.ReservationService;
import com.example.resourcereservation.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ResourceService resourceService;

    @GetMapping
    @Operation(summary = "Listar reservas", description = "Retorna uma lista de todas as reservas.")
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "reservations"; // Página que lista todas as reservas
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter reserva por ID", description = "Retorna os detalhes de uma reserva específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
        @ApiResponse(responseCode = "302", description = "Reserva não encontrada, redireciona para a lista de reservas")
    })
    public String getReservation(@Parameter(description = "ID da reserva") @PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation == null) {
            return "redirect:/reservations"; // Redireciona caso a reserva não seja encontrada
        }
        model.addAttribute("reservation", reservation);
        return "reservation-details"; // Página que mostra detalhes da reserva
    }

    @PostMapping
    @Operation(summary = "Criar nova reserva", description = "Cria uma nova reserva com os dados fornecidos.")
    public String createReservation(@Parameter(description = "Dados da reserva") @ModelAttribute Reservation reservation) {
        reservationService.saveReservation(reservation);
        return "redirect:/reservations"; // Redireciona para a lista de reservas
    }

    @PostMapping("/{id}")
    @Operation(summary = "Atualizar reserva", description = "Atualiza os dados de uma reserva existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva atualizada"),
        @ApiResponse(responseCode = "302", description = "Reserva não existe, redireciona para a lista de reservas")
    })
    public String updateReservation(@Parameter(description = "ID da reserva") @PathVariable Long id,
                                     @Parameter(description = "Dados atualizados da reserva") @ModelAttribute Reservation reservation) {
        if (reservationService.getReservationById(id) == null) {
            return "redirect:/reservations"; // Redireciona se a reserva não existir
        }
        reservation.setId(id);
        reservationService.saveReservation(reservation);
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/delete")
    @Operation(summary = "Excluir reserva", description = "Exclui uma reserva existente.")
    public String deleteReservation(@Parameter(description = "ID da reserva a ser excluída") @PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations";
    }

    @GetMapping("/availability")
    @Operation(summary = "Verificar disponibilidade", description = "Verifica a disponibilidade de um recurso entre duas datas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna as reservas dentro do intervalo de datas"),
        @ApiResponse(responseCode = "302", description = "Recurso não encontrado, redireciona para a lista de reservas")
    })
    public String checkAvailability(@Parameter(description = "ID do recurso") @RequestParam Long resourceId,
                                    @Parameter(description = "Data e hora de início") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                    @Parameter(description = "Data e hora de término") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                    Model model) {
        model.addAttribute("reservations", reservationService.getReservationsForResourceBetweenDates(resourceId, start, end));
        
        if (resourceService.getResourceById(resourceId) == null) {
            return "redirect:/reservations"; // Redireciona caso o recurso não seja encontrado
        }
        
        model.addAttribute("resource", resourceService.getResourceById(resourceId));
        return "availability"; // Página que mostra disponibilidade do recurso
    }
}
