package ar.edu.utn.frba.dds.auth;

import ar.edu.utn.frba.dds.auth.validadorContrasenia.ValidadorDeContrasenia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;


public class ValidadorContraseniaTest {

    @Test
    public void testContraseniaCorta()
    {ValidadorDeContrasenia validador = new ValidadorDeContrasenia();
        assertFalse(validador.validar("short"));}
    @Test
    public void testContraseniaUnicode()
    {ValidadorDeContrasenia validador = new ValidadorDeContrasenia();
    assertFalse(validador.validar("ÁÁÁÁÁÁ"));}

    @Test
    public void testContraseniaPopular()
    {ValidadorDeContrasenia validador = new ValidadorDeContrasenia();
        assertFalse(validador.validar("qwertyuiop"));}

    @Test
    public void testContraseniaValida()
    {ValidadorDeContrasenia validador = new ValidadorDeContrasenia();
        assertFalse(validador.validar("dds2024mm"));}

}
