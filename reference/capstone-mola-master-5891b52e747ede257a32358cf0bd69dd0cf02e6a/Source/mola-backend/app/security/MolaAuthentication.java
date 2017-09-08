package security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import controllers.HomeController;
import models.front.UserLogin;
import org.apache.shiro.subject.Subject;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;
import utils.Const;
import utils.Respond;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class MolaAuthentication extends Action<IMolaAuthentication> {

    @Inject
    FormFactory formFactory;
    @Inject
    private ExecutionContextExecutor exec;
    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        String authenString = ctx.request().getHeader(Const.HEADER_AUTHENTICATION);
        if (authenString == null || authenString.isEmpty()){
            return respondInvalidToken();
        }

        try {
            SignedJWT signedJWT = SignedJWT.parse(authenString);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            JWSVerifier verifier = new MACVerifier(Const.sharedSecret);
            if (!signedJWT.verify(verifier) || !jwtClaimsSet.getIssuer().equals(Const.issuer)){
                return respondInvalidToken();
            }
            if (jwtClaimsSet.getExpirationTime().before(new Date())){
                String message = "Token expired";
                Respond respond = new Respond("error", message);
                return CompletableFuture.completedFuture(ok(Json.toJson(respond)));
            }
            ctx.session().put("username", jwtClaimsSet.getSubject());



        } catch (ParseException e) {
            return respondInvalidToken();
        } catch (JOSEException e) {
            return respondInvalidToken();
        }


        return delegate.call(ctx);
    }


    private static CompletionStage<Result> respondInvalidToken(){
        String message = "Invalid token";
        Respond respond = new Respond("error", message);
        return CompletableFuture.completedFuture(ok(Json.toJson(respond)));
    }
}
