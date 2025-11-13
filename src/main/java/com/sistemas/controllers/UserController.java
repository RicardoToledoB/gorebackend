package com.sistemas.controllers;
import com.sistemas.dtos.UserDTO;
import com.sistemas.services.impl.UserServiceImpl;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN','ADMINISTRATIVO')")
public class UserController {


    @Autowired
    private UserServiceImpl service;

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }


    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body
    ) {
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        if (oldPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().build();
        }

        service.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Integer id, @RequestBody UserDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/getAllPaginated")
    public ResponseEntity<Page<UserDTO>> getAllPaginated(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.getAllPaginated(name, pageable));
    }


    /* SOFT DELETE */
    @GetMapping
    public ResponseEntity<List<UserDTO>> listActive() {
        return ResponseEntity.ok(service.listActive());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<UserDTO>> listDeleted() {
        return ResponseEntity.ok(service.listDeleted());
    }



    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        service.restore(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(defaultValue = "active") String type
    ) throws IOException {

        List<UserDTO> data = "deleted".equalsIgnoreCase(type)
                ? service.listDeleted()
                : service.listActive();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Usuarios");

            // === Encabezado ===
            String[] headers = {
                    "ID",
                    "Primer Nombre",
                    "Segundo Nombre",
                    "Primer Apellido",
                    "Segundo Apellido",
                    "Email",
                    "Usuario",
                    "RUT",
                    "Creado",
                    "Actualizado",
                    "Eliminado"
            };

            // Estilo encabezado
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // === Datos ===
            int rowIdx = 1;
            for (UserDTO u : data) {
                Row row = sheet.createRow(rowIdx++);
                int col = 0;

                row.createCell(col++).setCellValue(u.getId());
                row.createCell(col++).setCellValue(nvl(u.getFirstName()));
                row.createCell(col++).setCellValue(nvl(u.getSecondName()));
                row.createCell(col++).setCellValue(nvl(u.getFirstLastName()));
                row.createCell(col++).setCellValue(nvl(u.getSecondLastName()));
                row.createCell(col++).setCellValue(nvl(u.getEmail()));
                row.createCell(col++).setCellValue(nvl(u.getUsername()));
                row.createCell(col++).setCellValue(nvl(u.getRut()));
                row.createCell(col++).setCellValue(
                        u.getCreatedAt() != null ? u.getCreatedAt().toString() : ""
                );
                row.createCell(col++).setCellValue(
                        u.getUpdatedAt() != null ? u.getUpdatedAt().toString() : ""
                );
                row.createCell(col++).setCellValue(
                        u.getDeletedAt() != null ? u.getDeletedAt().toString() : ""
                );
            }

            // Auto-ajustar columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Congelar encabezado y aplicar autofiltro
            sheet.createFreezePane(0, 1);
            sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, headers.length - 1));

            wb.write(out);
        }

        byte[] bytes = out.toByteArray();
        String filename = "Usuarios_" + ("deleted".equalsIgnoreCase(type) ? "Eliminados" : "Activos") + ".xlsx";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + filename)
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(bytes);
    }

    // Helper para valores nulos
    private static String nvl(Object v) {
        return v == null ? "" : String.valueOf(v);
    }
}
