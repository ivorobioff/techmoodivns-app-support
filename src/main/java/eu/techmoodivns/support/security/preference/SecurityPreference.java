package eu.techmoodivns.support.security.preference;

import org.springframework.http.HttpMethod;

import java.util.HashSet;
import java.util.Set;

public class SecurityPreference {

    private Set<Endpoint> openEndpoints = new HashSet<>();

    public SecurityPreference addOpenEndpoint(HttpMethod method, String path) {
        openEndpoints.add(new Endpoint(method, path));
        return this;
    }

    public Set<Endpoint> getOpenEndpoints() {
        return openEndpoints;
    }

    public static class Endpoint {
        private HttpMethod method;
        private String path;

        public Endpoint(HttpMethod method, String path) {
            this.method = method;
            this.path = path;
        }

        public void setMethod(HttpMethod method) {
            this.method = method;
        }

        public HttpMethod getMethod() {
            return method;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
