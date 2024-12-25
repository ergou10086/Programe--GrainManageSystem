package entity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 封装粮库基本信息的类
// 实现Serializable接口以支持对象序列化，便于文件导出
public class WareHouse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // 仓库的编码类信息
    private String id;                  // 数据库主键
    private String warehouseCode;       // 仓库编码
    // 公司及负责人信息
    private String companyName;         // 公司名称
    private String responsiblePerson;   // 负责人
    // 仓库信息
    private String warehouseName;// 仓库名
    private String snowtime;
    private String humidity;
    private String temper;
    private String problem;
    private String deal;
    private String spectionpeople;
    private String spectiontime;
    private String problem2;
    private String deal2;
    private String problem3;
    private String deal3;
    private String notice;
    private String warehouseLocation;   // 仓库位置
    private BigDecimal capacity;        // 仓库库容
    private String structure;           // 仓库结构
    // 租赁信息
    private BigDecimal leaseAmount;     // 每月租金
    private LocalDate leaseStartDate;   // 租赁开始日期
    private LocalDate leaseEndDate;     // 租赁结束日期
    // 类型区分
    private String warehouseType;       // 仓库类型
    private String status;              // 仓库状态，库存为0的时候，不能被租出去

    // 构造方法
    public WareHouse() {}

    // 验证仓库信息是否有效，仓库信息不为空且库存大于0
    public boolean isValid() {
        return warehouseCode != null && !warehouseCode.trim().isEmpty() &&
               companyName != null && !companyName.trim().isEmpty() &&
               capacity != null && capacity.compareTo(BigDecimal.ZERO) > 0;
    }


    // 重写toString，方便信息导出
    @Override
    public String toString() {
        // 获取当前时间，并格式化为字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);

        // 拼接信息
        return warehouseCode + ";" + warehouseName + ";" + companyName + ";" + responsiblePerson + ";" +
               warehouseLocation + ";" + capacity + ";" + structure + ";" + warehouseType + ";" + formattedDate;
    }


    // Getter和Setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public BigDecimal getCapacity() {
        return capacity;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public BigDecimal getLeaseAmount() {
        return leaseAmount;
    }

    public void setLeaseAmount(BigDecimal leaseAmount) {
        this.leaseAmount = leaseAmount;
    }

    public LocalDate getLeaseStartDate() {
        return leaseStartDate;
    }

    public void setLeaseStartDate(LocalDate leaseStartDate) {
        this.leaseStartDate = leaseStartDate;
    }

    public LocalDate getLeaseEndDate() {
        return leaseEndDate;
    }

    public void setLeaseEndDate(LocalDate leaseEndDate) {
        this.leaseEndDate = leaseEndDate;
    }

    public String getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(String warehouseType) {
        this.warehouseType = warehouseType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getSpectiontime() {
        return spectiontime;
    }

    public void setSpectiontime(String spectiontime) {
        this.spectiontime = spectiontime;
    }

    public String getDeal() {
        return deal;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getProblem() {
        return problem;
    }

    public String getSnowtime() {
        return snowtime;
    }

    public String getSpectionpeople() {
        return spectionpeople;
    }

    public String getTemper() {
        return temper;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setSnowtime(String snowtime) {
        this.snowtime = snowtime;
    }

    public void setSpectionpeople(String spectionpeople) {
        this.spectionpeople = spectionpeople;
    }

    public void setTemper(String temper) {
        this.temper = temper;
    }

    public String getDeal2() {
        return deal2;
    }

    public String getDeal3() {
        return deal3;
    }

    public String getProblem2() {
        return problem2;
    }

    public String getProblem3() {
        return problem3;
    }

    public void setDeal2(String deal2) {
        this.deal2 = deal2;
    }

    public void setDeal3(String deal3) {
        this.deal3 = deal3;
    }

    public void setProblem2(String problem2) {
        this.problem2 = problem2;
    }

    public void setProblem3(String problem3) {
        this.problem3 = problem3;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getNotice() {
        return notice;
    }
}
