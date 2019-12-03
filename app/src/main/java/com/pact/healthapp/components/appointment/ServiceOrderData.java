package com.pact.healthapp.components.appointment;

import com.pact.healthapp.components.serviceorder.ServiceOrderDetailBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyl on 2015/12/23.
 */
public class ServiceOrderData {
    public static String costServiceCount = "1";//本次使用额度(非体检类使用，体检类1)

    /**
     * 预约首页,选择证卡类型,输入证卡号码,查询出对应用户
     */
    public static String certTypeId, certTypeName, certNumber;//本次预约流程使用的证卡类型和证卡名称和正卡号
    /**
     * 选择体检人
     */
    public static List<CheckupUserBean> users;//通过证卡查询出来的用户
    /**
     * 体检人
     */
    public static CheckupUserBean cb;//页面选中的用户
    public static int userinfoEditable = 0;//查询出的用户是否可编辑个人信息,0不可,1可以,2除证件类型号码外可以
    /**
     * 凭证
     */
    public static List<VoucherBean> vouchers;//根据用户查询凭证集合
    public static VoucherBean vb;//选中使用的凭证
    /**
     * 套餐,套餐详情
     */
    public static List<PackageBean> packages;//根据凭证查询套餐集合
    public static PackageBean pb;//本次预约流程选中的套餐对象
    public static PackageBean pbdetail;//套餐详情
    public static List<PackageitemBean> packageitems;//套餐详情原子项列表
    /**
     * 机构
     */
    public static List<OrgListBean> orgs = new ArrayList<OrgListBean>();//体检套餐查询出所有的机构
    public static OrgListBean ob;//本次预约流程选中的机构对象
    /**
     * 体检项,体检项筛选
     */
    public static List<AddItemBean> allAddItems;//机构查询所有体检项集合
    public static List<AddItemBean.PackageDatasEntity> checkedChildren = new ArrayList<AddItemBean.PackageDatasEntity>();//选中的原子项
    public static List<AddItemBean> checkedParents = new ArrayList<AddItemBean>();//选中的体检项包
    public static List<AddItemBean.PackageDatasEntity> selectedAddItems;//确认选中的体检项
    public static int startPrice = 0;
    public static int endPrice = 0;
    public static boolean isPersonalPay = true;
    public static boolean isXinhuaPay = true;
    public static boolean isCompanyPay = true;
    public static String keyword = "";//搜索关键字

    public static void clearAdditemFilter() {//清空筛选条件
        startPrice = 0;
        endPrice = 0;
        isPersonalPay = true;
        isXinhuaPay = true;
        isCompanyPay = true;
        keyword = "";
    }

    /**
     * 时间
     */
    public static List<ChoiceDateBean> dates;//所有的预约排期
    public static String choicedMonth;
    public static String choicedDay;
    public static ChoiceDateBean choicedTime;
    public static Map<String, List<ChoiceDateBean>> month_days_times;
    public static int startyear, startmonth, endyear, endmonth;
    public static List<ChoiceDateBean> list;

    public static void clearDate() {//清空时间页面数据
        choicedMonth = null;
        choicedDay = null;
        choicedTime = null;
        month_days_times = null;
        list = null;
        startyear = startmonth = endyear = endmonth = 0;
    }

    /**
     * 预览
     */
    public static double payable = -1;//个人应付
    public static double unpaid = -1;//个人未付金额
    public static double paid = -1;//个人已付金额
    public static double packagePriceSell = -1;//套餐的销售价格
    public static String companyPaidAmount;
    public static String nciPaidAmount;
    public static String freeAmount;
    public static boolean isPriceSell = false;
    public static List<PackageitemBean> underProductList;//承接机构承接的套餐中的原子项目列表

    public static void clearPrice() {//清空计算金额
        payable = -1;
        unpaid = -1;
        paid = -1;
        packagePriceSell = -1;
        companyPaidAmount = null;
        nciPaidAmount = null;
        freeAmount = null;
        underProductList = null;
    }


    public static String fromWhere = "Y"; //通过预约流程传Y    通过体检账户预约就传空
    public static int userssize;//用户列表长度(包含不可用)
    public static String whichday;//选中的天


    public static String serviceOrderId;//服务单Id
    public static String serviceOrderNo;//服务单号
    public static String selfPay;//支付金额
    public static String paymentOrderNum;//支付单号
    public static int paymentMethod;//支付方式

    public static void clearOrder() {
        serviceOrderId = null;
        serviceOrderNo = null;
        selfPay = null;
        paymentOrderNum = null;
        paymentMethod = 0;
    }

    public static ServiceOrderDetailBean orderDetail;//服务单详情
    public static String operation;//记录是改期还是预约

    //重选证卡类型
    public static void setCertTypeId(String certTypeId) {
        ServiceOrderData.certTypeId = certTypeId;
        cb = null;
        users = null;
        vb = null;
        vouchers = null;
        pb = null;
        packages = null;
        ob = null;
        orgs = null;
        checkedChildren = new ArrayList<AddItemBean.PackageDatasEntity>();
        checkedParents = new ArrayList<AddItemBean>();
        selectedAddItems = null;
        clearAdditemFilter();
        clearDate();
        clearPrice();
        clearOrder();
    }

    //重选证卡类型
    public static void setCertTypeName(String certTypeName) {
        ServiceOrderData.certTypeName = certTypeName;
        cb = null;
        users = null;
        vb = null;
        vouchers = null;
        pb = null;
        packages = null;
        ob = null;
        orgs = null;
        checkedChildren = new ArrayList<AddItemBean.PackageDatasEntity>();
        checkedParents = new ArrayList<AddItemBean>();
        selectedAddItems = null;
        clearAdditemFilter();
        clearDate();
        clearPrice();
        clearOrder();
    }

    //重填证卡号
    public static void setCertNumber(String certNumber) {
        ServiceOrderData.certNumber = certNumber;
        cb = null;
        users = null;
        vb = null;
        vouchers = null;
        pb = null;
        packages = null;
        ob = null;
        orgs = null;
        checkedChildren = new ArrayList<AddItemBean.PackageDatasEntity>();
        checkedParents = new ArrayList<AddItemBean>();//选中的体检项包
        selectedAddItems = null;
        clearAdditemFilter();
        clearDate();
        clearPrice();
        clearOrder();
    }

    //重选用户
    public static void setCb(CheckupUserBean cb) {
        ServiceOrderData.cb = cb;
        vb = null;
        vouchers = null;
        pb = null;
        packages = null;
        ob = null;
        orgs = null;
        checkedChildren = new ArrayList<AddItemBean.PackageDatasEntity>();
        checkedParents = new ArrayList<AddItemBean>();//选中的体检项包
        selectedAddItems = null;
        clearAdditemFilter();
        clearDate();
        clearPrice();
        clearOrder();
    }

    //重选凭证
    public static void setVb(VoucherBean vb) {
        ServiceOrderData.vb = vb;
        pb = null;
        packages = null;
        orgs = null;
        ob = null;
        dates = null;
        checkedChildren = new ArrayList<AddItemBean.PackageDatasEntity>();
        checkedParents = new ArrayList<AddItemBean>();//选中的体检项包
        selectedAddItems = null;
        clearAdditemFilter();
        clearDate();
        clearPrice();
        clearOrder();
    }

    //重选套餐
    public static void setPb(PackageBean pb) {
        ServiceOrderData.pb = pb;
        orgs = null;
        ob = null;
        dates = null;
        allAddItems = null;
        clearAdditemFilter();
        checkedChildren = new ArrayList<AddItemBean.PackageDatasEntity>();
        checkedParents = new ArrayList<AddItemBean>();//选中的体检项包
        selectedAddItems = null;
        clearDate();
        clearPrice();
        clearOrder();
    }

    //重选机构
    public static void setOb(OrgListBean ob) {
        ServiceOrderData.ob = ob;
        allAddItems = null;
        clearAdditemFilter();
        checkedChildren = new ArrayList<AddItemBean.PackageDatasEntity>();
        checkedParents = new ArrayList<AddItemBean>();//选中的体检项包
        selectedAddItems = null;
        dates = null;
        clearDate();
        clearPrice();
        clearOrder();
    }

    //重选体检项
    public static void setSelectedAddItems(List<AddItemBean.PackageDatasEntity> selectedAddItems) {
        ServiceOrderData.selectedAddItems = new ArrayList<AddItemBean.PackageDatasEntity>();
        ServiceOrderData.selectedAddItems.addAll(selectedAddItems);
        dates = null;
        whichday = null;
        clearDate();
        clearPrice();
        clearOrder();
    }

    //重选时间
    public static void setChoicedTime(ChoiceDateBean choicedTime) {
        ServiceOrderData.choicedTime = choicedTime;
        clearPrice();
        clearOrder();
    }


    public static void setOrderDetail(ServiceOrderDetailBean orderDetail) {
        ServiceOrderData.orderDetail = orderDetail;
    }

    /**
     * 清空所有数据
     */
    public static void clearData() {
        costServiceCount = null;
        certTypeId = null;
        certTypeName = null;
        certNumber = null;
        fromWhere = "Y";
        cb = null;
        vb = null;
        pb = null;
        ob = null;
        users = null;
        userssize = 0;
        vouchers = null;
        packages = null;
        pbdetail = null;
        packageitems = null;
        allAddItems = null;
        clearAdditemFilter();
        checkedParents = new ArrayList<AddItemBean>();
        checkedChildren = new ArrayList<AddItemBean.PackageDatasEntity>();
        selectedAddItems = null;
        orderDetail = null;
        operation = null;
        clearDate();
        clearPrice();
        clearOrder();
    }


}
