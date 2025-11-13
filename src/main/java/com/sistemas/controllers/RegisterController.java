package com.sistemas.controllers;

import com.sistemas.dtos.RegisterDTO;
import com.sistemas.services.impl.RegisterServiceImpl;
import com.sistemas.services.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
// imports clave:
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/registers")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN','ADMINISTRATIVO')")
public class RegisterController {
    @Autowired
    private RegisterServiceImpl service;

    @PostMapping
    public ResponseEntity<RegisterDTO> create(@RequestBody RegisterDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/deleted-paginated")
    public ResponseEntity<Page<RegisterDTO>> getDeletedPaginated(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.getDeletedPaginated(search, pageable));
    }


    @GetMapping("/active-paginated")
    public ResponseEntity<Page<RegisterDTO>> getActivePaginated(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.getActivePaginated(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<RegisterDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }



    @PutMapping("/{id}")
    public ResponseEntity<RegisterDTO> update(@PathVariable Integer id, @RequestBody RegisterDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/getAllPaginated")
    public ResponseEntity<Page<RegisterDTO>> getAllPaginated(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        // Si no se envía "search", el parámetro será null, y el repository lo ignora.
        return ResponseEntity.ok(service.getAllPaginated(search, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/all")
    public ResponseEntity<List<RegisterDTO>> getAll() {
        return ResponseEntity.ok(service.listAll());
    }



    /* SOFT DELETE */
    @GetMapping
    public ResponseEntity<List<RegisterDTO>> listActive() {
        return ResponseEntity.ok(service.listActive());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<RegisterDTO>> listDeleted() {
        return ResponseEntity.ok(service.listDeleted());
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        service.restore(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary-by-stablishment")
    public ResponseEntity<List<Map<String, Object>>> getSummaryByStablishment() {
        return ResponseEntity.ok(service.countRegistersByStablishment());
    }


    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(defaultValue = "active") String type,
            @RequestParam(required = false) String search
    ) throws IOException {

        // 1) Datos según tipo y (opcional) filtro
        List<RegisterDTO> data;
        if ("deleted".equalsIgnoreCase(type)) {
            if (search != null && !search.isBlank()) {
                // si deseas que el filtro aplique también a eliminados, puedes crear un método service
                // paginado que devuelva lista completa (o usa tu repo nativo con LIKE) y mapear a DTO.
                data = service.listDeleted().stream()
                        .filter(r -> r.getDescription_property() != null &&
                                r.getDescription_property().toLowerCase().contains(search.toLowerCase()))
                        .toList();
            } else {
                data = service.listDeleted();
            }
        } else {
            if (search != null && !search.isBlank()) {
                data = service.listActive().stream()
                        .filter(r -> r.getDescription_property() != null &&
                                r.getDescription_property().toLowerCase().contains(search.toLowerCase()))
                        .toList();
            } else {
                data = service.listActive();
            }
        }

        // 2) Construcción Excel
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Inventario");

            // Estilos
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setWrapText(true);

            // 3) Encabezados (TODOS los campos de RegisterEntity + útiles de relaciones)
            String[] headers = new String[] {
                    "id",
                    "n_inventary",
                    "date_reception",
                    "n_memo",
                    "project",
                    "financing",
                    "n_acta_reception",
                    "date_acta_reception",
                    "purchase_order",
                    "acquisition_value",
                    "provider",
                    "rut_provider",
                    "n_fact",
                    "date_fact",
                    "amount_fact",
                    "n_dispatch_guide",
                    "date_dispatch_guide",
                    "amount_dispatch_guide",
                    "description_property",
                    "brand",
                    "model",
                    "n_serie",
                    "n_res_info",
                    "date_res_info",
                    // Relación Stablishment
                    "stablishment_id",
                    "stablishment_name",
                    // Extras
                    "observation_state",
                    "n_res_gore",
                    "date_res_gore",
                    "n_res_accept",
                    "date_res_accept",
                    "state",
                    // Relación User
                    "user_id",
                    "user_first_name",
                    "user_first_last_name",
                    "user_email",
                    // Timestamps
                    "createdAt",
                    "updatedAt",
                    "deletedAt"
            };

            // Header row
            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }

            // 4) Datos
            int rowIdx = 1;
            for (RegisterDTO r : data) {
                Row row = sheet.createRow(rowIdx++);

                int col = 0;
                row.createCell(col++).setCellValue(nvl(r.getId()));
                row.createCell(col++).setCellValue(nvl(r.getN_inventary()));
                row.createCell(col++).setCellValue(nvl(r.getDate_reception()));
                row.createCell(col++).setCellValue(nvl(r.getN_memo()));
                row.createCell(col++).setCellValue(nvl(r.getProject()));
                row.createCell(col++).setCellValue(nvl(r.getFinancing()));
                row.createCell(col++).setCellValue(nvl(r.getN_acta_reception()));
                row.createCell(col++).setCellValue(nvl(r.getDate_acta_reception()));
                row.createCell(col++).setCellValue(nvl(r.getPurchase_order()));
                row.createCell(col++).setCellValue(nvl(r.getAcquisition_value()));
                row.createCell(col++).setCellValue(nvl(r.getProvider()));
                row.createCell(col++).setCellValue(nvl(r.getRut_provider()));
                row.createCell(col++).setCellValue(nvl(r.getN_fact()));
                row.createCell(col++).setCellValue(nvl(r.getDate_fact()));
                row.createCell(col++).setCellValue(nvl(r.getAmount_fact()));
                row.createCell(col++).setCellValue(nvl(r.getN_dispatch_guide()));
                row.createCell(col++).setCellValue(nvl(r.getDate_dispatch_guide()));
                row.createCell(col++).setCellValue(nvl(r.getAmount_dispatch_guide()));
                row.createCell(col++).setCellValue(nvl(r.getDescription_property()));
                row.createCell(col++).setCellValue(nvl(r.getBrand()));
                row.createCell(col++).setCellValue(nvl(r.getModel()));
                row.createCell(col++).setCellValue(nvl(r.getN_serie()));
                row.createCell(col++).setCellValue(nvl(r.getN_res_info()));
                row.createCell(col++).setCellValue(nvl(r.getDate_res_info()));

                // Stablishment
                row.createCell(col++).setCellValue(r.getStablishment() != null ? nvl(r.getStablishment().getId()) : "");
                row.createCell(col++).setCellValue(r.getStablishment() != null ? nvl(r.getStablishment().getName()) : "");

                // Otros
                row.createCell(col++).setCellValue(nvl(r.getObservation_state()));
                row.createCell(col++).setCellValue(nvl(r.getN_res_gore()));
                row.createCell(col++).setCellValue(nvl(r.getDate_res_gore()));
                row.createCell(col++).setCellValue(nvl(r.getN_res_accept()));
                row.createCell(col++).setCellValue(nvl(r.getDate_res_accept()));
                row.createCell(col++).setCellValue(nvl(r.getState()));

                // User
                row.createCell(col++).setCellValue(r.getUser() != null ? nvl(r.getUser().getId()) : "");
                row.createCell(col++).setCellValue(r.getUser() != null ? nvl(r.getUser().getFirstName()) : "");
                row.createCell(col++).setCellValue(r.getUser() != null ? nvl(r.getUser().getFirstLastName()) : "");
                row.createCell(col++).setCellValue(r.getUser() != null ? nvl(r.getUser().getEmail()) : "");

                // Fechas
                row.createCell(col++).setCellValue(r.getCreatedAt() != null ? r.getCreatedAt().toString() : "");
                row.createCell(col++).setCellValue(r.getUpdatedAt() != null ? r.getUpdatedAt().toString() : "");
                row.createCell(col++).setCellValue(r.getDeletedAt() != null ? r.getDeletedAt().toString() : "");
            }

            // Auto-size columnas
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            wb.write(out);
        }

        byte[] bytes = out.toByteArray();
        String filename = "Inventario_" + ("deleted".equalsIgnoreCase(type) ? "deleted" : "active")
                + (search != null && !search.isBlank() ? "_filter" : "")
                + ".xlsx";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + filename)
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(bytes);
    }

    // Helpers (puedes ponerlos como métodos privados en el controller o llevarlos a una utilidad)
    private static String nvl(Object v) { return v == null ? "" : String.valueOf(v); }

}
