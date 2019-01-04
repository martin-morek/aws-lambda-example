import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;

import java.util.List;

public class LambdaExample implements RequestHandler<S3EventNotification, String> {

    static final AWSCredentials credentials = new BasicAWSCredentials(
            "<AccessKey>",
            "<SecretKey>"
    );

    static final String SOURCE_S3_BUCKET = "<SourceBucketName>";
    static final String DESTINATION_S3_BUCKET = "<DestinationBucketName>";

    static final AmazonS3 s3client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.EU_CENTRAL_1)
            .build();

    public String handleRequest(S3EventNotification s3EventNotification, Context context) {
        List<S3EventNotification.S3EventNotificationRecord> s3EventRecords = s3EventNotification.getRecords();

        for(S3EventNotification.S3EventNotificationRecord event : s3EventRecords) {
            final String objectName = event.getS3().getObject().getKey();

            s3client.copyObject(
                    SOURCE_S3_BUCKET,
                    objectName,
                    DESTINATION_S3_BUCKET,
                    objectName);
        }
        return null;
    }
}
