package br.com.ma.meusgastos.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class HelloMicroservice extends AbstractVerticle {


  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(HelloMicroservice.class.getName());
  }

    @Override
    public void start() {
        System.out.println("Start....");

        Router router = Router.router(vertx);
        router.get("/").handler(rc -> rc.response().end("hello"));
        router.get("/:name").handler(this::hello);
        vertx.createHttpServer()
          .requestHandler(router::accept)
          .listen(8080, "0.0.0.0");//Integer.getInteger("http.port"), System.getProperty("http.address", "0.0.0.0"));
    }

    private void hello(RoutingContext rc) {
        System.out.println("Routing....");
    	String message = "Hello";
    	if(rc.pathParam("name")!= null) {
    		message += " " + rc.pathParam("name");
    	}
    	JsonObject json = new JsonObject().put("message", message);
    	rc.response()
    		.putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    		.end(json.encode());
    }
    
    private void createJWT(RoutingContext rc){
  		JWTAuthOptions config = new JWTAuthOptions()
  				.addPubSecKey(new PubSecKeyOptions()
  						 .setAlgorithm("HS256")
  						 .setPublicKey("LJHOJowiueroj349iPOrertpi034t5kgçesdmgltngh")
  						 .setSymmetric(true));
  		
  		AuthProvider provider = JWTAuth.create(Vertx.vertx(), config);
  		provider.authenticate(new JsonObject().put("jwt"
  				                                   , "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjU5MmYyOGMyZDU4ZTkxMGQ0ZGMxMzViMyIsImV4cCI6MTUzMzEzNDM2NDA3Mn0.8dut2zqC2jJnm8nzh2OZjAJpvByq4PDY9ieUVLE0Ows"), res -> {
  			if (res.succeeded()) {
  				User theUser = res.result();
  				System.out.println(theUser.principal().toString());
  			} else {
  				System.out.println(res);
  				// Failed!
  			}
  		});
  		
    }
}


