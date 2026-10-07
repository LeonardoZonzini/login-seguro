package com.loginseguro.app.security;

import com.loginseguro.app.config.SecurityConfig;
import com.loginseguro.app.controller.AdminUsuariosController;
import com.loginseguro.app.controller.AutenticacaoController;
import com.loginseguro.app.controller.PainelController;
import com.loginseguro.app.service.AutenticacaoService;
import com.loginseguro.app.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AutenticacaoController.class, PainelController.class, AdminUsuariosController.class})
@Import(SecurityConfig.class)
class AutorizacaoWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private AutenticacaoService autenticacaoService;

    @Test
    void loginEhPublico() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    void painelExigeAutenticacao() throws Exception {
        mockMvc.perform(get("/painel")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "USUARIO")
    void usuarioComumNaoAcessaPainelAdmin() throws Exception {
        mockMvc.perform(get("/painel/admin")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminAcessaPainelAdmin() throws Exception {
        mockMvc.perform(get("/painel/admin")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USUARIO")
    void usuarioComumAcessaPainelProprio() throws Exception {
        mockMvc.perform(get("/painel/usuario")).andExpect(status().isOk());
    }

}
