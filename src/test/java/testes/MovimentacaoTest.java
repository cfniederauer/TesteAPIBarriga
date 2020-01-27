package testes;

import Objetos.Conta;
import Objetos.Movimentacao;
import core.BaseTest;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MovimentacaoTest extends BaseTest {

    private String TOKEN;
    private static String CONTA_NAME = "Conta " + System.nanoTime();
    private static Integer CONTA_ID;
    private static Integer MOV_ID;

    @Before
    public void fazerLogin(){
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "caio@teste");
        login.put("senha","teste123");

        TOKEN = given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token");

    }

    @Test
    public void test01_incluirContaComSucesso(){
        Conta conta = new Conta();
        conta.setNome(CONTA_NAME);

        CONTA_ID = given()
                .header("Authorization", "JWT " + TOKEN) //bearer
                .body(conta)
                .when()
                .post("/contas")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    @Test
    public void test02_inserirMovimentacaoComSucesso(){
        Movimentacao movi = new Movimentacao();
        movi.setConta_id(CONTA_ID);
        movi.setDescricao("Descricao teste");
        movi.setEnvolvido("Envolvido teste");
        movi.setTipo("DESP");
        movi.setData_transacao("22/01/2018");
        movi.setData_pagamento("23/01/2018");
        movi.setValor(55f);
        movi.setStatus(true);

        MOV_ID = given()
                .header("Authorization", "JWT " + TOKEN) //bearer
                .body(movi)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    @Test
    public void test03_removerMovimentacao(){
        given()
                .header("Authorization", "JWT " + TOKEN) //bearer
                .pathParam("id", MOV_ID)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204);

    }
}
