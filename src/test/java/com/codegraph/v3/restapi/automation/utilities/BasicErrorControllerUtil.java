package com.codegraph.v3.restapi.automation.utilities;

import com.aurea.automation.codegraph.oa3.api.BasicErrorControllerApi;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

/**
 * Utility class for api calls in basic-error-controller group.
 */

@UtilityClass
public class BasicErrorControllerUtil {
    private static BasicErrorControllerApi api() {
        return ApiUtils.apiClient().basicErrorController();
    }


    /**
     * Path   : "/error".
     * Method : get
     * OpId   : errorHtmlUsingGET
     * undefined
     *
     * @return ModelAndView
     */
    public static Response errorHtmlUsingGET(
            Options.OptionsBuilder<BasicErrorControllerApi.ErrorHtmlUsingGETOper>... options) {
        return Options.execute(api()
                .errorHtmlUsingGET(), options);

    }


    /**
     * Path   : "/error"
     * Method : head
     * OpId   : errorHtmlUsingHEAD
     * undefined
     *
     * @return ModelAndView
     */
    public static Response errorHtmlUsingHEAD(
            Options.OptionsBuilder<BasicErrorControllerApi.ErrorHtmlUsingHEADOper>... options) {
        return Options.execute(api()
                .errorHtmlUsingHEAD(), options);

    }


    /**
     * Path   : "/error"
     * Method : post
     * OpId   : errorHtmlUsingPOST
     * undefined
     *
     * @return ModelAndView
     */
    public static Response errorHtmlUsingPOST(
            Options.OptionsBuilder<BasicErrorControllerApi.ErrorHtmlUsingPOSTOper>... options) {
        return Options.execute(api()
                .errorHtmlUsingPOST(), options);

    }


    /**
     * Path   : "/error"
     * Method : put
     * OpId   : errorHtmlUsingPUT
     * undefined
     *
     * @return ModelAndView
     */
    public static Response errorHtmlUsingPUT(
            Options.OptionsBuilder<BasicErrorControllerApi.ErrorHtmlUsingPUTOper>... options) {
        return Options.execute(api()
                .errorHtmlUsingPUT(), options);

    }


    /**
     * Path   : "/error"
     * Method : delete
     * OpId   : errorHtmlUsingDELETE
     * undefined
     *
     * @return ModelAndView
     */
    public static Response errorHtmlUsingDELETE(
            Options.OptionsBuilder<BasicErrorControllerApi.ErrorHtmlUsingDELETEOper>... options) {
        return Options.execute(api()
                .errorHtmlUsingDELETE(), options);

    }


    /**
     * Path   : "/error"
     * Method : options
     * OpId   : errorHtmlUsingOPTIONS
     * undefined
     *
     * @return ModelAndView
     */
    public static Response errorHtmlUsingOPTIONS(
            Options.OptionsBuilder<BasicErrorControllerApi.ErrorHtmlUsingOPTIONSOper>... options) {
        return Options.execute(api()
                .errorHtmlUsingOPTIONS(), options);

    }


    /**
     * Path   : "/error"
     * Method : patch
     * OpId   : errorHtmlUsingPATCH
     * undefined
     *
     * @return ModelAndView
     */
    public static Response errorHtmlUsingPATCH(
            Options.OptionsBuilder<BasicErrorControllerApi.ErrorHtmlUsingPATCHOper>... options) {
        return Options.execute(api()
                .errorHtmlUsingPATCH(), options);

    }


}
