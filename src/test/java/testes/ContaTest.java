package testes;

import Objetos.Conta;
import core.BaseTest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContaTest extends BaseTest {

    private String TOKEN;
    private static String CONTA_NAME = "Conta " + System.nanoTime();
    private static Integer CONTA_ID;

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
                .extract().path("id")
        ;
    }

    @Test
    public void test02_alterarContaComSucesso(){
        given()
                .header("Authorization", "JWT " + TOKEN) //bearer
                .body("{\"nome\" : \""+CONTA_NAME+" alterada\" }")
                .pathParam("id", CONTA_ID)
                .when()
                .put("/contas/{id}")
                .then()
                .statusCode(200)
                .body("nome", Matchers.is(CONTA_NAME+" alterada"));
    }

    @Test
    public void test03_consultarContaComSucesso(){
        given()
                .header("Authorization", "JWT " + TOKEN) //bearer
                //.body("{\"nome\" : \""+CONTA_NAME+" alterada\" }")
                .pathParam("id", CONTA_ID)
                .when()
                .get("/contas/{id}")
                .then()
                .statusCode(200)
                .body("nome", Matchers.is(CONTA_NAME+" alterada"));

    }
    @Test
    public void test04_deletarContaComSucesso(){
        given()
                .header("Authorization", "JWT " + TOKEN) //bearer
                .pathParam("id", CONTA_ID)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(204);

    }

    @Test
    public void test05_consultarContaDeletada(){
        given()
                .header("Authorization", "JWT " + TOKEN) //bearer
                //.body("{\"nome\" : \""+CONTA_NAME+" alterada\" }")
                .pathParam("id", CONTA_ID)
                .when()
                .get("/contas/{id}")
                .then()
                .statusCode(404)
                .body("error", Matchers.is("Conta com id "+CONTA_ID+" não encontrada"));

    }

}
