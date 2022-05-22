package raf.si.bolnica.laboratory.requests;

public class SetStatusExaminationDTO {
    private Long id;
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
