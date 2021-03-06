package de.uni.freiburg.iig.telematik.jagal.graph.exception;

import de.uni.freiburg.iig.telematik.jagal.graph.Edge;
import de.uni.freiburg.iig.telematik.jagal.graph.Vertex;
import de.uni.freiburg.iig.telematik.jagal.graph.abstr.AbstractGraph;

public class NoMatchingVertexException extends GraphException {

        private static final long serialVersionUID = 1L;
        private final String messagePart = "No vertex found, that contains element \"";
        private final String element;

        public <V extends Vertex<U>, E extends Edge<V>, U> NoMatchingVertexException(String element, AbstractGraph<V, E, U> graph) {
                super(graph.getName());
                this.element = element;
        }

        @Override
        public String getMessage() {
                return messagePart + element + "\"";
        }

        public String getElement() {
                return element;
        }

        public enum VertexError {

                VERTEX_NOT_IN_GRAPH(" does not contain vertex "),
                NO_MATCHING_VERTEX("No matching vertex");

                private final String message;

                VertexError(String message) {
                        this.message = message;
                }

                public String message() {
                        return message;
                }

        }
}
