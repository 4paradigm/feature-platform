package com._4paradigm.openmldb.featureplatform.client.examples;

import com._4paradigm.openmldb.featureplatform.client.FeaturePlatformClient;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SingleTableSingleFeatureViewExample {

    public static void main(String[] args) {
        FeaturePlatformClient client = new FeaturePlatformClient("127.0.0.1", 8888);

        try {
            // Create test db and tables
            client.executeSql("CREATE DATABASE IF NOT EXISTS t1v1");
            client.executeSql("CREATE TABLE IF NOT EXISTS t1v1.user (name string, age int, user_code string)");

            // Create feature view
            client.createFeatureView("t1v1_v1", "", "t1v1", "SELECT name, age + 10 AS new_age, user_code FROM user");

            // Create feature service
            client.createFeatureService("t1v1_s1", "t1v1_v1");

            // Test feature service
            HttpResponse response = client.requestFeatureService("t1v1_s1", "{\"input\": [[\"abc\", 22]]}");
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
