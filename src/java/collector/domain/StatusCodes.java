package collector.domain;

/**
 * Created by benjamin on 7/17/14.
 */
public enum StatusCodes {
    STATUS_REQUEST_RECEIVED,
    STATUS_REQUEST_ERROR,
    STATUS_GENERATION_PENDING,
    STATUS_GENERATION_REJECTED,
    STATUS_GENERATION_FAILED,
    STATUS_GENERATION_SUCCESSFUL,
    STATUS_GENERATION_UNKNOWN,
    STATUS_DELIVERY_PENDING,
    STATUS_DELIVERY_FAILED,
    STATUS_DELIVERY_SUCCESSFUL,
    STATUS_DELIVERY_UNKNOWN,
    STATUS_DELIVERY_SUPPRESSED_EMAIL,
    STATUS_DELIVERY_ERROR,
    STATUS_QC_PENDING,
    STATUS_QC_FAILED,
    STATUS_QC_SUCCESSFUL,
    STATUS_QC_UNKNOWN;

    @Override
    public String toString() {
        switch(this) {
            case STATUS_REQUEST_RECEIVED:
                return "REQUEST_RECEIVED";
            case STATUS_REQUEST_ERROR:
                return "REQUEST_ERROR";
            case STATUS_GENERATION_PENDING:
                return "GENERATION_PENDING";
            case STATUS_GENERATION_REJECTED:
                return "GENERATION_REJECTED";
            case STATUS_GENERATION_FAILED:
                return "GENERATION_FAILED";
            case STATUS_GENERATION_SUCCESSFUL:
                return "GENERATION_SUCCESSFUL";
            case STATUS_GENERATION_UNKNOWN:
                return "GENERATION_UNKNOWN";
            case STATUS_DELIVERY_PENDING:
                return "DELIVERY_PENDING";
            case STATUS_DELIVERY_FAILED:
                return "DELIVERY_FAILED";
            case STATUS_DELIVERY_SUCCESSFUL:
                return "DELIVERY_SUCCESSFUL";
            case STATUS_DELIVERY_UNKNOWN:
                return "DELIVERY_UNKNOWN";
            case STATUS_DELIVERY_SUPPRESSED_EMAIL:
                return "DELIVERY_SUPPRESSED_EMAIL";
            case STATUS_DELIVERY_ERROR:
                return "DELIVERY_ERROR";
            case STATUS_QC_PENDING:
                return "QC_PENDING";
            case STATUS_QC_FAILED:
                return "QC_FAILED";
            case STATUS_QC_SUCCESSFUL:
                return "QC_SUCCESSFUL";
            case STATUS_QC_UNKNOWN:
                return "QC_UNKNOWN";
            default:
                return null;
        }
    }


    /*REQUEST_RECEIVED,
    REQUEST_ERROR,
    GENERATION_PENDING,
    GENERATION_REJECTED,
    GENERATION_FAILED,
    GENERATION_SUCCESSFUL,
    GENERATION_UNKNOWN,
    DELIVERY_PENDING,
    DELIVERY_FAILED,
    DELIVERY_SUCCESSFUL,
    DELIVERY_UNKNOWN,
    DELIVERY_SUPPRESSED_EMAIL,
    DELIVERY_ERROR,
    QC_PENDING,
    QC_FAILED,
    QC_SUCCESSFUL,
    QC_UNKNOWN;*/

    /*REQUEST_RECEIVED(1),
    REQUEST_ERROR(2),
    GENERATION_PENDING(3),
    GENERATION_REJECTED(4),
    GENERATION_FAILED(5),
    GENERATION_SUCCESSFUL(6),
    GENERATION_UNKNOWN(7),
    DELIVERY_PENDING(8),
    DELIVERY_FAILED(9),
    DELIVERY_SUCCESSFUL(10),
    DELIVERY_UNKNOWN(11),
    DELIVERY_SUPPRESSED_EMAIL(12),
    DELIVERY_ERROR(13),
    QC_PENDING(14),
    QC_FAILED(15),
    QC_SUCCESSFUL(16),
    QC_UNKNOWN(17);

    public int value;
    private StatusCodes(int value){
        this.value = value;
    }*/

};