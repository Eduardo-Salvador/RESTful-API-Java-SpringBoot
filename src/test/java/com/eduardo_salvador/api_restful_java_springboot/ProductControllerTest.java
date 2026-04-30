package com.eduardo_salvador.api_restful_java_springboot;
import com.eduardo_salvador.api_restful_java_springboot.controllers.ProductController;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductRecordDto;
import com.eduardo_salvador.api_restful_java_springboot.dtos.ProductResponseDto;
import com.eduardo_salvador.api_restful_java_springboot.exceptions.NoFindException;
import com.eduardo_salvador.api_restful_java_springboot.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void findById_IfExists_Returns200() throws Exception {
        UUID id = UUID.randomUUID();
        ProductResponseDto productResponseDto = new ProductResponseDto(id, "Test Product", new BigDecimal("10.0"));

        when(productService.findById(id)).thenReturn(productResponseDto);

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(10.0));
    }

    @Test
    void findById_IfNoExists_Returns404() throws Exception {
        UUID id = UUID.randomUUID();

        when(productService.findById(id)).thenThrow(new NoFindException("Product not found."));

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found."));
    }

    @Test
    void saveProduct_IfValid_Returns201() throws Exception {
        ProductRecordDto requestDto = new ProductRecordDto("Notebook", BigDecimal.valueOf(3000));
        UUID id = UUID.randomUUID();
        ProductResponseDto responseDto = new ProductResponseDto(id, "Notebook", BigDecimal.valueOf(3000));

        when(productService.save(any(ProductRecordDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProduct").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Notebook"));
    }

    @Test
    void saveProduct_WithBlankName_Returns400() throws Exception {
        String invalidBody = """
            {
              "name": "",
              "price": 3000.00
            }
            """;
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveProduct_WhenMissingFields_Returns400() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteProduct_IfExists_Returns200() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(productService).delete(id);

        mockMvc.perform(delete("/products/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProduct_WhenNotExists_Returns404() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(new NoFindException("Product not found."))
                .when(productService).delete(id);

        mockMvc.perform(delete("/products/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProduct_IfExists_Returns200() throws Exception {
        UUID id = UUID.randomUUID();
        ProductRecordDto requestDto = new ProductRecordDto("Notebook Pro", BigDecimal.valueOf(4500));
        ProductResponseDto responseDto = new ProductResponseDto(id, "Notebook Pro", BigDecimal.valueOf(4500));

        when(productService.update(eq(id), any(ProductRecordDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProduct").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Notebook Pro"))
                .andExpect(jsonPath("$.price").value(4500));

        verify(productService).update(eq(id), any(ProductRecordDto.class));
    }

    @Test
    void updateProduct_WhenNotExists_Returns404() throws Exception {
        UUID id = UUID.randomUUID();
        ProductRecordDto requestDto = new ProductRecordDto("Notebook Pro", BigDecimal.valueOf(4500));

        when(productService.update(eq(id), any(ProductRecordDto.class)))
                .thenThrow(new NoFindException("Product not found."));

        mockMvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found."));
    }

    @Test
    void updateProduct_WithBlankName_Returns400() throws Exception {
        UUID id = UUID.randomUUID();
        String invalidBody = """
            {
              "name": "",
              "price": 4500.00
            }
            """;

        mockMvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllProducts_WithNoFilters_Returns200() throws Exception {
        UUID id = UUID.randomUUID();
        ProductResponseDto responseDto = new ProductResponseDto(id, "Notebook", BigDecimal.valueOf(3000));

        Page<ProductResponseDto> page = new PageImpl<>(List.of(responseDto));

        when(productService.findAll(isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idProduct").value(id.toString()))
                .andExpect(jsonPath("$.content[0].name").value("Notebook"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getAllProducts_WithNameFilter_Returns200() throws Exception {
        UUID id = UUID.randomUUID();
        ProductResponseDto responseDto = new ProductResponseDto(id, "Notebook", BigDecimal.valueOf(3000));

        Page<ProductResponseDto> page = new PageImpl<>(List.of(responseDto));

        when(productService.findAll(eq("Notebook"), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/products").param("name", "Notebook"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Notebook"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getAllProducts_WhenEmpty_Returns404() throws Exception {
        when(productService.findAll(isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenThrow(new NoFindException("No products found."));

        mockMvc.perform(get("/products"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No products found."));
    }
}