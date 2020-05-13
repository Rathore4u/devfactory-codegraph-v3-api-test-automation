package com.codegraph.v3.restapi.automation.utilities;

import com.aurea.automation.codegraph.oa3.Oper;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.Responses;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import lombok.Builder;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Builder
public class Options<T extends Oper<T>> {
    private Consumer<T> customizer;
    private Function<Response, Response> handler;

    public void customize(T op) {
        if (customizer != null) {
            customizer.accept(op);
        }
    }

    /**
     * Use this option to store the response in the feature registry
     * <p>
     * @param name name of the response in the feature registry
     * @param <T> The type of operation
     * @return the option builder
     */
    public static <T extends Oper<T>> OptionsBuilder<T> storeAs(String name) {
        OptionsBuilder<T> builder = builder();
        return builder.handler(response -> Responses.setResponse(name, response));
    }

    public Response handleResponse(Response resp) {
        if (handler != null) {
            return handler.apply(resp);
        }
        return resp;
    }

    public static <T extends Oper<T>> OptionsBuilder<T> logRequest() {
        OptionsBuilder<T> builder = builder();
        return builder.customizer(op -> op.reqSpec(spec -> spec.log(LogDetail.ALL)));
    }

    public static <T extends Oper<T>> OptionsBuilder<T> logResponse() {
        OptionsBuilder<T> builder = builder();
        return builder
                .handler(response -> {
                    response.then().log().all();
                    return response;
                });
    }

    public static <T extends Oper<T>> OptionsBuilder<T> custom(Consumer<T> customizer) {
        OptionsBuilder<T> builder = builder();
        return builder.customizer(customizer);
    }

    public static <T extends Oper<T>> Response execute(T op, OptionsBuilder<T>[] builders) {
        Stream.of(builders).map(OptionsBuilder::build).forEach(o -> o.customize(op));
        Function<Response, Response> handler = response -> Stream.of(builders).map(OptionsBuilder::build)
                .reduce(response, (r, o) -> o.handleResponse(response), (first, last) -> last);
        return op.execute(handler);
    }
}
