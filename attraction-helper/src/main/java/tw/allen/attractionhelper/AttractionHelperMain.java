package tw.allen.attractionhelper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AttractionHelperMain {

    private static Scanner sc;
    private static Member loginuser;

    public static void printGuide() {
        System.out.println("歡迎登入台南景點小幫手! 請輸入數字選項");
        System.out.println("1) 查詢所有台南景點");
        System.out.println("2) 手動新增台南景點");
        System.out.println("3) 手動更新台南景點");
        System.out.println("4) 手動刪除台南景點");
        System.out.println("5) 進階功能(讀取政府開放資料 json 檔案)");
        System.out.println("6) 離開台南景點小幫手");
    }

    public static void printAdvanced() {
        System.out.println("1) 讀取台南景點 json 檔案");
        System.out.println("2) 自動新增台南景點(for json)");
        System.out.println("3) 匯出台南景點 json 檔案");
        System.out.println("4) 讀取台南景點 csv 檔案");
        System.out.println("5) 自動新增台南景點 csv 檔案");
        System.out.println("6) 匯出台南景點 csv 檔案");
        System.out.println("7) 匯出 Excel 檔案");
        System.out.println("8) 離開進階功能");

    }

    public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
        IMemberDao mdao = MemberDaoFactory.createMemberDao();
        IAttractionDao adao = AttractionDaoFactory.createAttractionDao();
        mdao.createConn();

        // Login Member username and password
        boolean loginSucc = false;
        while (loginSucc == false) {
            System.out.println("請輸入您的帳號與密碼(帳號 密碼請以空白當做間隔) ex: mary p@ssw0rd");
            sc = new Scanner(System.in);
            String userinfo = sc.nextLine();
            String[] infoArray = userinfo.split(" ");

            if (userinfo.isEmpty()) {
                System.out.println("無法輸入空值, 請重新輸入 or q 離開");
                continue;
            } else if (userinfo.equalsIgnoreCase("q")) {
                System.out.println("登出中...");
                break;
            } else if (infoArray.length < 2) {
                System.out.println("您缺少帳號或密碼, 請重新輸入");
                continue;
            }

            loginuser = mdao.existed(infoArray[0], infoArray[1]);
            if (loginuser.getUser_Id() > 0) {
                loginSucc = true;
            } else {
                System.out.println("Your username or password is uncorrect! Please try again!");
            }
        }
        adao.createConn();
        while (loginSucc) {
            printGuide();

            if (sc.hasNextInt()) {
                int choose = sc.nextInt();
                switch (choose) {
                case 1:
                    adao.queryAll();
                    break;
                case 2:
                    adao.addAttraction();
                    break;
                case 3:
                    adao.updateAttraction();
                    break;
                case 4:
                    adao.deleteAttraction();
                    break;
                case 5:
                    boolean advancedMethod = true;
                    while (advancedMethod) {
                        printAdvanced();

                        if (sc.hasNextInt()) {
                            int advancedchoose = sc.nextInt();
                            switch (advancedchoose) {
                            case 1:
                                adao.readJsonAttraction();
                                break;
                            case 2:{
                                List<Attraction> attractions = adao.readJsonAttraction();
                                adao.addJsonAttraction(attractions);
                                break;
                            }
                            case 3:
                                adao.writeJsonAttraction();
                                break;
                            case 4:
                                adao.readCsvAttraction();
                                break;
                            case 5:{
                                List<Attraction> attractions = adao.readCsvAttraction();
                                adao.addCsvAttraction(attractions);
                                break;
                            }
                            case 6:
                                adao.writeCsvAttraction();
                                break;
                            case 7:
                                adao.writeExcelAttraction();
                                break;
                            case 8:
                                System.out.println("離開進階功能");
                                advancedMethod = false;
                                break;
                            default:
                                System.out.println("沒有此數字選項");
                                break;
                            }
                        } else {
                            String badOption = sc.next();
                            System.out.println("您所輸入的選項為非數字選項: " + badOption + ", 請重新輸入選項");
                        }
                    }

                    break;
                case 6:
                    System.out.println("離開台南小幫手, 很高興能為您服務!");
                    loginSucc = false;
                    break;
                default:
                    System.out.println("沒有此數字選項");
                    break;
                }

            } else {
                String badOption = sc.next();
                System.out.println("您所輸入的選項為非數字選項: " + badOption + ", 請重新輸入選項");
            }

        }

        // 讀取 json file
        // List<Attraction> attractions = adao.readJsonAttraction();
        // 新增 json data
        // adao.addJsonAttraction(attractions);
        // 寫出 json file
        // adao.writeJsonAttraction();
        // 手動新增 Attraction data
        // adao.addAttraction();
        adao.closeConn();
        mdao.closeConn();

    }
}
