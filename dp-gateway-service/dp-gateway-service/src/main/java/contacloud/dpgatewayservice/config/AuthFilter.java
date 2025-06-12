package contacloud.dpgatewayservice.config;

import contacloud.dpgatewayservice.dto.TokenDto;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClient;

    public AuthFilter(WebClient.Builder webClient) {
        super(Config.class);
        this.webClient = webClient;
    }

    // Rutas públicas (no requieren autenticación)
    private static final List<String> publicRoutes = List.of(
            "/productos", "/productos/", "/productos/.*"
    );

    // Rutas y roles permitidos
    private static final Map<String, Map<String, List<String>>> roleRestrictions = Map.of(
            "/ventas(/.*)?", Map.of(
                    "GET", List.of("CLIENTE"),
                    "POST", List.of("CLIENTE")
            ),
            "/productos(/.*)?", Map.of(
                    "POST", List.of("ADMINISTRADOR"),
                    "PUT", List.of("ADMINISTRADOR"),
                    "PATCH", List.of("ADMINISTRADOR")
            ),
            "/movimientos(/.*)?", Map.of(
                    "GET", List.of("ADMINISTRADOR"),
                    "POST", List.of("ADMINISTRADOR")
            ),
            "/clientes(/.*)?", Map.of(
                    "GET", List.of("ADMINISTRADOR"),
                    "PUT", List.of("ADMINISTRADOR")
            ),
            "/licencias(/.*)?", Map.of(
                    "GET", List.of("CLIENTE")
            ),
            "/facturas(/.*)?", Map.of(
                    "GET", List.of("CLIENTE", "ADMINISTRADOR")
            )
    );

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            String method = exchange.getRequest().getMethod().name();

            // Verifica si la ruta es pública
            for (String route : publicRoutes) {
                if (path.matches(route)) {
                    return chain.filter(exchange); // sin token
                }
            }

            // Verifica presencia del token
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String tokenHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String[] chunks = tokenHeader.split(" ");
            if (chunks.length != 2 || !chunks[0].equals("Bearer")) {
                return onError(exchange, HttpStatus.BAD_REQUEST);
            }

            String token = chunks[1];

            return webClient.build()
                    .post()
                    .uri("http://auth-service/auth/validate?token=" + token)
                    .retrieve()
                    .bodyToMono(TokenDto.class)
                    .flatMap(tokenDto -> {
                        String rol = tokenDto.getRol();
                        if (!isAuthorized(path, method, rol)) {
                            return onError(exchange, HttpStatus.FORBIDDEN);
                        }
                        return chain.filter(exchange);
                    });
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    private boolean isAuthorized(String path, String method, String rol) {
        for (String pattern : roleRestrictions.keySet()) {
            if (path.matches(pattern)) {
                Map<String, List<String>> methodRoles = roleRestrictions.get(pattern);
                List<String> allowedRoles = methodRoles.get(method);
                return allowedRoles != null && allowedRoles.contains(rol);
            }
        }
        return false;
    }

    public static class Config {}
}
