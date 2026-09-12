package tn.formation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.formation.entities.CategorieProduit;
import tn.formation.services.ICategorieProduitService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategorieProduitControllerTest {

    @Mock
    private ICategorieProduitService categorieProduitService;

    @InjectMocks
    private CategorieProduitController categorieProduitController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private CategorieProduit categorieProduit1;
    private CategorieProduit categorieProduit2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categorieProduitController).build();
        objectMapper = new ObjectMapper();

        categorieProduit1 = new CategorieProduit();
        categorieProduit1.setIdCategorieProduit(1L);
        categorieProduit1.setCodeCategorie("CAT01");
        categorieProduit1.setLibelleCategorie("Électronique");

        categorieProduit2 = new CategorieProduit();
        categorieProduit2.setIdCategorieProduit(2L);
        categorieProduit2.setCodeCategorie("CAT02");
        categorieProduit2.setLibelleCategorie("Alimentaire");
    }

    @Test
    void testGetCategorieProduit_ReturnsList() throws Exception {
        List<CategorieProduit> list = Arrays.asList(categorieProduit1, categorieProduit2);
        when(categorieProduitService.retrieveAllCategorieProduits()).thenReturn(list);

        mockMvc.perform(get("/categorieProduit/retrieve-all-categorieProduit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].idCategorieProduit").value(1))
                .andExpect(jsonPath("$[0].codeCategorie").value("CAT01"))
                .andExpect(jsonPath("$[1].idCategorieProduit").value(2));

        verify(categorieProduitService, times(1)).retrieveAllCategorieProduits();
    }

    @Test
    void testRetrieveCategorieProduit_ReturnsSingleItem() throws Exception {
        when(categorieProduitService.retrieveCategorieProduit(1L)).thenReturn(categorieProduit1);

        mockMvc.perform(get("/categorieProduit/retrieve-categorieProduit/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategorieProduit").value(1))
                .andExpect(jsonPath("$.libelleCategorie").value("Électronique"));

        verify(categorieProduitService, times(1)).retrieveCategorieProduit(1L);
    }

    @Test
    void testAddCategorieProduit_ReturnsCreatedEntity() throws Exception {
        when(categorieProduitService.addCategorieProduit(any(CategorieProduit.class)))
                .thenReturn(categorieProduit1);

        mockMvc.perform(post("/categorieProduit/add-categorieProduit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categorieProduit1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategorieProduit").value(1));

        verify(categorieProduitService, times(1)).addCategorieProduit(any(CategorieProduit.class));
    }

    @Test
    void testRemoveCategorieProduit_CallsServiceDelete() throws Exception {
        doNothing().when(categorieProduitService).deleteCategorieProduit(anyLong());

        mockMvc.perform(delete("/categorieProduit/remove-categorieProduit/1"))
                .andExpect(status().isOk());

        verify(categorieProduitService, times(1)).deleteCategorieProduit(1L);
    }

    @Test
    void testModifyCategorieProduit_ReturnsUpdatedEntity() throws Exception {
        when(categorieProduitService.updateCategorieProduit(any(CategorieProduit.class)))
                .thenReturn(categorieProduit1);

        mockMvc.perform(put("/categorieProduit/modify-categorieProduit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categorieProduit1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategorieProduit").value(1));

        verify(categorieProduitService, times(1)).updateCategorieProduit(any(CategorieProduit.class));
    }
}