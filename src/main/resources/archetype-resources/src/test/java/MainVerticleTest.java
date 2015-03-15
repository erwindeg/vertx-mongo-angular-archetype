#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import nl.sogeti.MainVerticle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class MainVerticleTest {

    private Vertx vertx;

    @Before
    public void setUp(TestContext context) {
      Async async = context.async();
      vertx = Vertx.vertx();
      vertx.deployVerticle(MainVerticle.class.getName(), ar -> {
          if (ar.succeeded()) {
              async.complete();
          } else {
              context.fail("Could not deploy verticle");
          }
      }); 
    }

    @Test
    public void testHello(TestContext context) {
        Async async = context.async();
        HttpClient client = vertx.createHttpClient();
        HttpClientRequest req = client.get(8080, "localhost", "/api/hello-world");
        req.exceptionHandler(err -> {
            context.fail();
        });
        req.handler(resp -> {
            context.assertEquals(200, resp.statusCode());
            Buffer entity = Buffer.buffer();
            resp.handler(entity::appendBuffer);
            resp.endHandler(v -> {
                context.assertEquals("{\"content\" : \"Hello world!\" }", entity.toString("UTF-8"));
                async.complete();
            });
        });
        req.end();
    }

    @After
    public void tearDown(TestContext context) {
        Async async = context.async();
        vertx.close(ar -> {
            async.complete();
        });
    }
}
