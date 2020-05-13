package com.codegraph.v3.restapi.automation.utilities;

import com.aurea.automation.codegraph.oa3.api.WebhookApi;
import com.aurea.automation.codegraph.oa3.models.StackBuildStatusUpdateDto;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

/**
 * Utility class for api calls in webhook group
 */

@UtilityClass
public class WebhookUtil {
    private static WebhookApi api() {
        return ApiUtils.apiClient().webhook();
    }


    /**
     * Path   : "/stackbuilds/{id}/updates"
     * Method : post
     * OpId   : saveStackAndLayerUpdatesUsingPOST
     * Send stack and layer build updates
     *
     * @return StackBuildRequest
     */
    public static Response saveStackAndLayerUpdatesUsingPOST(
            String id,
            StackBuildStatusUpdateDto body,
            Options.OptionsBuilder<WebhookApi.SaveStackAndLayerUpdatesUsingPOSTOper>... options) {
        return Options.execute(api()
                .saveStackAndLayerUpdatesUsingPOST()
                .idPath(id)
                .body(body), options);

    }


}
