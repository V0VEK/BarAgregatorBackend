package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;


@EnableScheduling
@RestController
public class AppHandler {

    String STATUS_USER_EXIST = "USER_EXIST";
    String STATUS_WRONG_TOKEN = "WRONG_TOKEN";
    String STATUS_WRONG_PASSWORD = "WRONG_PASSWORD";
    String STATUS_WRONG_OTP = "WRONG_OTP";
    String STATUS_OK = "OK";
    String STATUS_ERROR = "ERROR";
    String STATUS_USER_NOT_EXISTS = "USER_NOT_EXISTS";
    String STATUS_RECIPT_NOT_EXISTS = "RECEIPT_NOT_EXISTS";
    String STATUS_SESSION_NOT_EXISTS = "SESSION_TIMEOUT";

    String ROLE_ADMIN = "admin";
    String ROLE_USER = "user";
    String ROLE_BAR_ADMIN = "bar_admin";
    String ROLE_WAITER = "waiter";

    String YES = "YES";
    String NO = "NO";

    String USER_ROLE_ID = "user";

    Integer PASS_LENGTH = 7;

    int OTP_LENGTH = 4;
    Integer FLAG_PAID = 1;

    String MAIL_FROM = "vldmr.rr3@gmail.com";
    String MAIL_ACCESS = "ACCESS";
    String MAIL_OTP = "OTP";
    String EASY_PASS = "123";


    @Autowired
    UserInfoDBRepository userInfoDBRepository;

    @Autowired
    BarInfoDBRepository barInfoDBRepository;

    @Autowired
    ReceiptProductsDBRepository receiptProductsDBRepository;

    @Autowired
    WaiterInfoDBRepository waiterInfoDBRepository;

    @Autowired
    BarProductsDBRepository barProductsDBRepository;

    @Autowired
    ReceiptInfoDBRepository receiptInfoDBRepository;

    @Autowired
    public JavaMailSender emailSender;

    @RequestMapping("/registration")
    public ResponseEntity<StatusResponse> Registration(@RequestBody UserCredentials uc) {

        System.out.println("Pass_hash: " + uc.pass_hash + "\nEmail: " + uc.email);

        StatusResponse status = new StatusResponse();

        // Check if user credentials present in DB
        if (!userInfoDBRepository.findByEmail(uc.email).isEmpty()) {
            status.setStatus(STATUS_USER_EXIST);
        }
        else {
            status.setStatus(STATUS_OK);

            String otp = GenerateOTP(OTP_LENGTH);
            System.out.println(otp);

            // Get data from user's request to write it in DB
            UserInfoDB user = new UserInfoDB();
            user.setEmail(uc.email);
            user.setPasswordHash(uc.pass_hash);
            user.setRoleID(USER_ROLE_ID);
            user.setOtp(otp);

            // Write to DB user credentials
            userInfoDBRepository.save(user);
        }

        // Response to the client
        return new ResponseEntity<>(status, HttpStatus.OK);
    }


    @RequestMapping("/otp_confirmation")
    public ResponseEntity<UserToken> ConfirmOTP(@RequestBody OTPConfirmation otpConfirmation) {

        System.out.println("EMAIL: " + otpConfirmation.getEmail() + " OTP: " + otpConfirmation.getOtp());
        UserToken ut = new UserToken();
        if (userInfoDBRepository.findByOtpAndEmail(otpConfirmation.getOtp(), otpConfirmation.getEmail()).isEmpty()) {
            ut.setStatus(STATUS_WRONG_OTP);
            ut.setUser_token(STATUS_ERROR);
        }
        else {
            ut.setStatus(STATUS_OK);
            ut.setUser_token(GenerateUserToken());

            UserInfoDB userInfoDB = userInfoDBRepository.findByEmail(otpConfirmation.getEmail()).get(0);
            userInfoDB.setUserToken(ut.getUser_token());

            userInfoDBRepository.save(userInfoDB);
        }
        return new ResponseEntity<>(ut, HttpStatus.OK);
    }


    @RequestMapping("/authorization")
    public ResponseEntity<SessionTokenForUser> Authorization (@RequestBody FA2 fa2) {

        SessionTokenForUser userRole = GetSession(fa2.pass_hash, fa2.user_token);
        System.out.println("TOKEN: " + fa2.getUser_token());
        System.out.println("PASSHASH: " + fa2.getPass_hash());

        return new ResponseEntity<>(userRole, HttpStatus.OK);
    }

    private SessionTokenForUser GetSession(String passHash, String token) {
        SessionTokenForUser user = new SessionTokenForUser();

        if (userInfoDBRepository.findByUserTokenAndPasswordHash(token, passHash).isEmpty()) {
           if (!userInfoDBRepository.findByPasswordHash(passHash).isEmpty()) {
                /*user.setStatus(STATUS_WRONG_TOKEN);
                user.setUser_role_id(STATUS_ERROR);
                user.setSession_token(STATUS_ERROR);*/

                user.setSession_token(GenerateUserToken());

                // Without 2FA
                UserInfoDB session = userInfoDBRepository.findByPasswordHash(passHash).get(0);
                session.setSessionToken(user.getSession_token());
                session.setSessionCreationTime(System.currentTimeMillis());

                userInfoDBRepository.save(session);
                user.setStatus(STATUS_OK);
                user.setUser_role_id(session.getRoleID());
            }
           else if (!userInfoDBRepository.findByUserToken(token).isEmpty()) {
                user.setStatus(STATUS_WRONG_PASSWORD);
                user.setUser_role_id(STATUS_ERROR);
                user.setSession_token(STATUS_ERROR);
            }
           else {
               user.setStatus(STATUS_WRONG_PASSWORD);
               user.setUser_role_id(STATUS_ERROR);
               user.setSession_token(STATUS_ERROR);
           }
        }
        else {
            user.setStatus(STATUS_OK);
            user.setSession_token(GenerateUserToken());

            UserInfoDB session = userInfoDBRepository.findByUserToken(token).get(0);
            session.setSessionToken(user.getSession_token());
            session.setSessionCreationTime(System.currentTimeMillis());

            userInfoDBRepository.save(session);
            // Set role as in DB
            user.setUser_role_id(userInfoDBRepository.findByUserToken(token).get(0).getRoleID());
        }
        return user;
    }


    @RequestMapping("/otp_req")
    public ResponseEntity<StatusResponse> OTPRequest(@RequestBody UserEmail email) {
        StatusResponse status = new StatusResponse();

        if (userInfoDBRepository.findByEmail(email.getEmail()).isEmpty()) {
            status.setStatus(STATUS_USER_NOT_EXISTS);
        }
        else {
            UserInfoDB newOTP = userInfoDBRepository.findByEmail(email.getEmail()).get(0);

            String otp = GenerateOTP(OTP_LENGTH);

            newOTP.setOtp(otp);
            newOTP.setOtpCreationTime(System.currentTimeMillis());
            newOTP.setOtpUsed(NO);
            userInfoDBRepository.save(newOTP);

            SendSimpleMessage(email.getEmail(), MAIL_OTP, otp);

            status.setStatus(STATUS_OK);
        }

        return new ResponseEntity<>(status, HttpStatus.OK);
    }


    @RequestMapping("/waiter")
    public ResponseEntity<WaiterReceiptList> WaiterInterface(@RequestBody UserSession sess) {

        WaiterReceiptList receiptList = new WaiterReceiptList();

        if (userInfoDBRepository.findBySessionTokenAndRoleID(sess.getSession_token(), ROLE_WAITER).isEmpty()) {
            receiptList.setStatus(STATUS_SESSION_NOT_EXISTS);
        }
        else {
            receiptList = HandleWaiterInterface(sess.getSession_token());
        }

        return new ResponseEntity<>(receiptList, HttpStatus.OK);
    }

    @RequestMapping("/add_receipt")
    public ResponseEntity<StatusResponse> AddReceipt(@RequestBody AddReceiptReq receiptReq) {
        StatusResponse status = new StatusResponse();

        System.out.println(receiptReq.getSession_token());
        System.out.println(receiptReq.getBar_receipt_id());
        System.out.println(receiptReq.getProducts_id());


        if(userInfoDBRepository.findBySessionTokenAndRoleID(receiptReq.getSession_token(), ROLE_WAITER).isEmpty()) {
            status.setStatus(STATUS_SESSION_NOT_EXISTS);
        }
        else {
            AddReceiptInDB(receiptReq.getProducts_id(), receiptReq.getBar_receipt_id(), receiptReq.getSession_token());
            status.setStatus(STATUS_OK);
        }

        return new ResponseEntity<>(status, HttpStatus.OK);
    }


    private void AddReceiptInDB(List<Integer> productsIDs, Integer barReceiptID, String sessionToken) {

        ReceiptInfoDB uniqueReceipt = new ReceiptInfoDB();
        Integer waiterID = userInfoDBRepository.findBySessionToken(sessionToken).get(0).getUserID();
        Integer barID = barInfoDBRepository.findByWaiterID(waiterID).get(0).getBarID();

        uniqueReceipt.setBarID(barID);
        uniqueReceipt.setBarReceiptID(barReceiptID);
        uniqueReceipt.setWaiterID(waiterID);

        receiptInfoDBRepository.save(uniqueReceipt);

        Integer uniqueID = receiptInfoDBRepository.findByBarReceiptIDAndWaiterID(barReceiptID, waiterID).get(0).getUniqueReceiptID();

        for (Integer productID : productsIDs) {
            ReceiptProductsDB receipt = new ReceiptProductsDB();
            receipt.setProductID(productID);
            receipt.setWaiterID(waiterID);
            receipt.setUniqueReceiptID(uniqueID);

            receiptProductsDBRepository.save(receipt);
        }
    }


    private WaiterReceiptList HandleWaiterInterface(String sessionToken) {
        WaiterReceiptList result = new WaiterReceiptList();
        result.setStatus(STATUS_OK);

        Integer waiterID = userInfoDBRepository.findBySessionToken(sessionToken).get(0).getUserID();
        List<ReceiptProductsDB> receiptList = receiptProductsDBRepository.findByWaiterID(waiterID);
        List<Integer> receiptNumList = new ArrayList<>();

        for (ReceiptProductsDB receipt : receiptList) {
            receiptNumList.add(receipt.getUniqueReceiptID());
        }

        result.setReceipt_numbers(receiptNumList);

        return result;
    }


    @RequestMapping("/get_bar_products")
    public ResponseEntity<BarProductsList> ShowBarProducts(@RequestBody UserSession sess) {
        BarProductsList prodList = new BarProductsList();

        if (userInfoDBRepository.findBySessionTokenAndRoleID(sess.getSession_token(), ROLE_WAITER).isEmpty()) {
            prodList.setStatus(STATUS_SESSION_NOT_EXISTS);
        }
        else {
            prodList = FillProductsList(sess.getSession_token());
        }

        return new ResponseEntity<>(prodList, HttpStatus.OK);
    }


    private BarProductsList FillProductsList(String sessionToken) {
        BarProductsList result = new BarProductsList();
        result.setStatus(STATUS_OK);

        Integer waiterID = userInfoDBRepository.findBySessionToken(sessionToken).get(0).getUserID();
        Integer barID = waiterInfoDBRepository.findByWaiterID(waiterID).get(0).getBarID();
        List<BarProductsDB> barProducts = barProductsDBRepository.findByBarID(barID);

        List<String> prodNames = new ArrayList<>();
        List<Integer> prodIDs = new ArrayList<>();

        for (BarProductsDB product : barProducts) {
            prodNames.add(product.getProductName());
            prodIDs.add(product.getProductID());
        }
        result.setProducts_list(prodNames);
        result.setProducts_id(prodIDs);

        return result;
    }


    @RequestMapping("/waiter/delete_receipt")
    public ResponseEntity<StatusResponse> DeleteReceipt(@RequestBody ReceiptToDelete deleteReq) {
        StatusResponse status = new StatusResponse();

        if (userInfoDBRepository.findBySessionTokenAndRoleID(deleteReq.getSessionToken(), ROLE_WAITER).isEmpty()) {
            status.setStatus(STATUS_SESSION_NOT_EXISTS);
        }

        else {

        }

        return new ResponseEntity<>(status, HttpStatus.OK);
    }


    private void DeleteReceiptFromDB(Integer uniqueReceiptID) {

    }

    // Used by admin role too
    @RequestMapping("/get_bar_list")
    public ResponseEntity<BarList> GetBarList(@RequestBody UserSession sess) {
        BarList barList = new BarList();

        if (userInfoDBRepository.findBySessionToken(sess.getSession_token()).isEmpty()) {
            barList.setStatus(STATUS_SESSION_NOT_EXISTS);
        }
        else {
            barList = FillBarList();
            barList.setStatus(STATUS_OK);
        }

        return new ResponseEntity<>(barList, HttpStatus.OK);
    }


    private BarList FillBarList() {
        BarList result = new BarList();

        List<UserInfoDB> barsIDs = userInfoDBRepository.findByRoleID(ROLE_BAR_ADMIN);

        List<String> names = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        for (UserInfoDB barID : barsIDs) {
            BarInfoDB bar = barInfoDBRepository.findByBarID(barID.getUserID()).get(0);
            names.add(bar.getBarName());
            ids.add(bar.getBarID());
        }

        System.out.println("BARLIST: " + names.toString());

        result.setBar_ids(ids);
        result.setBar_names(names);

        return result;
    }


    @RequestMapping("/add_bar")
    public ResponseEntity<StatusResponse> AddBarAdmin(@RequestBody AdminAddBarReq addBarReq) {
        StatusResponse sr = new StatusResponse();

        System.out.println(addBarReq.getBar_name() + " " + addBarReq.getEmail());

        if (userInfoDBRepository.findBySessionTokenAndRoleID(addBarReq.getSession_token(), ROLE_ADMIN).isEmpty()) {
            sr.setStatus(STATUS_SESSION_NOT_EXISTS);
        }
        else if (!userInfoDBRepository.findByEmail(addBarReq.getEmail()).isEmpty()) {
            sr.setStatus(STATUS_USER_EXIST);
        }

        else {
                HandleAddingBar(addBarReq.getEmail(), addBarReq.getBar_name());
                sr.setStatus(STATUS_OK);
            }
        return new ResponseEntity<> (sr, HttpStatus.OK);
    }


    private void HandleAddingBar(String email, String barName) {

        String password;
        String passHash;
        String token;

        password = GenerateRandomString(PASS_LENGTH);
        passHash = CalculateSHA256(password);
        token = GenerateUserToken();

        UserInfoDB barAdmin = new UserInfoDB();

        barAdmin.setEmail(email);
        barAdmin.setUserToken(token);
        barAdmin.setRoleID(ROLE_BAR_ADMIN);
        barAdmin.setPasswordHash(passHash);

        userInfoDBRepository.save(barAdmin);

        Integer barID = userInfoDBRepository.findByUserToken(token).get(0).getUserID();

        BarInfoDB bar = new BarInfoDB();
        bar.setBarID(barID);
        bar.setBarName(barName);

        barInfoDBRepository.save(bar);

        AddProductsForBar(barID);

        SendSimpleMessage(email, MAIL_ACCESS, password);
    }


    private void AddProductsForBar(Integer barID) {
        List<String> prodNames = new ArrayList<>();
        List<Integer> prodPrice = new ArrayList<>();
        prodNames.add("Tea"); prodNames.add("Beer"); prodNames.add("Soup");
        prodNames.add("Burger"); prodNames.add("Meat"); prodNames.add("Potatoes");

        prodPrice.add(100); prodPrice.add(200); prodPrice.add(400);
        prodPrice.add(450); prodPrice.add(500); prodPrice.add(200);


        for (String prod : prodNames) {
            BarProductsDB product = new BarProductsDB();
            product.setBarID(barID);
            product.setProductName(prod);
            barProductsDBRepository.save(product);
        }
    }


    @RequestMapping("/delete_bar")
    public ResponseEntity<StatusResponse> DeleteBarAdmin(@RequestBody DeleteBarReq deleteReq) {
        StatusResponse status = new StatusResponse();

        if (userInfoDBRepository.findBySessionToken(deleteReq.getSession_token()).isEmpty()) {
            status.setStatus(STATUS_SESSION_NOT_EXISTS);
        }
        else if (userInfoDBRepository.findByUserID(deleteReq.getBar_id()).isEmpty()){
            status.setStatus(STATUS_USER_NOT_EXISTS);

        }

        else {
            HandleDeletingBar(deleteReq.getBar_id());
            status.setStatus(STATUS_OK);

        }

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    private void HandleDeletingBar(Integer barID) {
        // Delete from DB
        userInfoDBRepository.deleteById(barID);
        barInfoDBRepository.deleteById(barID);
    }


    @RequestMapping("/get_waiters_list")
    public ResponseEntity<WaitersList> BarAdminInterface(@RequestBody UserSession sess) {
        WaitersList waitersList = new WaitersList();

        if (userInfoDBRepository.findBySessionTokenAndRoleID(sess.getSession_token(), ROLE_BAR_ADMIN).isEmpty()) {
            waitersList.setStatus(STATUS_SESSION_NOT_EXISTS);
        }
        else {
            waitersList = FillWaitersList(sess.getSession_token());
        }

        return new ResponseEntity<>(waitersList, HttpStatus.OK);
    }

    private WaitersList FillWaitersList(String sessionToken) {
        WaitersList result = new WaitersList();

        Integer barID = userInfoDBRepository.findBySessionToken(sessionToken).get(0).getUserID();
        List <WaiterInfoDB> waiters = waiterInfoDBRepository.findByBarID(barID);

        List<String> names = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        for (WaiterInfoDB waiter : waiters) {
            names.add(waiter.getWaiterName());
            ids.add(waiter.getWaiterID());
        }

        result.setWaiters_id(ids);
        result.setWaiters_name(names);

        return result;
    }

    @RequestMapping("/add_waiter")
    public ResponseEntity<StatusResponse> AddWaiter(@RequestBody AddWaiterReq addWaiterReq) {
        StatusResponse status = new StatusResponse();

        if (userInfoDBRepository.findBySessionToken(addWaiterReq.getSession_token()).isEmpty()) {
            status.setStatus(STATUS_SESSION_NOT_EXISTS);
        }
        else if (!userInfoDBRepository.findByEmail(addWaiterReq.getEmail()).isEmpty()) {
            status.setStatus(STATUS_USER_EXIST);
        }
        else {
            Integer barID = userInfoDBRepository.findBySessionToken(addWaiterReq.getSession_token()).get(0).getUserID();
            HandleWaiterAdding(addWaiterReq.getEmail(), addWaiterReq.getWaiter_name(), barID);
            status.setStatus(STATUS_OK);
        }

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    private void HandleWaiterAdding(String waiterEmail, String waiterName, Integer barID) {
        String password;
        String passHash;
        String token;
        UserInfoDB waiter = new UserInfoDB();

        password = GenerateRandomString(PASS_LENGTH);
        passHash = CalculateSHA256(password);
        token = GenerateUserToken();

        waiter.setPasswordHash(passHash);
        waiter.setRoleID(ROLE_WAITER);
        waiter.setUserToken(token);
        waiter.setEmail(waiterEmail);

        userInfoDBRepository.save(waiter);

        Integer waiterID = userInfoDBRepository.findByUserToken(token).get(0).getUserID();

        WaiterInfoDB waiterInfo = new WaiterInfoDB();

        waiterInfo.setBarID(barID);
        waiterInfo.setWaiterID(waiterID);
        waiterInfo.setWaiterName(waiterName);

        waiterInfoDBRepository.save(waiterInfo);

        SendSimpleMessage(waiterEmail, MAIL_ACCESS, password);
    }

    @RequestMapping("/delete_waiter")
    public ResponseEntity<WaitersList> DeleteWaiter(@RequestBody DeleteWaiterReq deleteWaiterReq) {
        WaitersList waitersList = new WaitersList();
        if (userInfoDBRepository.findBySessionToken(deleteWaiterReq.getSession_token()).isEmpty()) {
            waitersList.setStatus(STATUS_SESSION_NOT_EXISTS);
        }
        else if (userInfoDBRepository.findByUserID(deleteWaiterReq.getWaiter_id()).isEmpty()) {
            waitersList.setStatus(STATUS_USER_NOT_EXISTS);
        }
        else {
            HandleWaiterDeleting(deleteWaiterReq.getWaiter_id());
            waitersList = FillWaitersList(deleteWaiterReq.getSession_token());
        }
        return new ResponseEntity<>(waitersList, HttpStatus.OK);
    }

    private void HandleWaiterDeleting(Integer waiterID) {
        // Delete from DB
        userInfoDBRepository.deleteById(waiterID);
        waiterInfoDBRepository.findById(waiterID);
    }


    @RequestMapping("/get_bar_receipt")
    public ResponseEntity<ReceiptResponse> GetBarReceipt(@RequestBody BarReceiptReq barReceiptReq) {
        ReceiptResponse receipt = new ReceiptResponse();

        if (userInfoDBRepository.findBySessionTokenAndRoleID(barReceiptReq.getSession_token(), ROLE_USER).isEmpty()) {
            receipt.setStatus(STATUS_SESSION_NOT_EXISTS);
        }
        else if (receiptInfoDBRepository.findByBarIDAndBarReceiptID(barReceiptReq.getBar_id(), barReceiptReq.getBar_receipt_id()).isEmpty()) {
            receipt.setStatus(STATUS_RECIPT_NOT_EXISTS);
        }
        else {
            ReceiptInfoDB r = receiptInfoDBRepository.findByBarIDAndBarReceiptID(barReceiptReq.getBar_id(), barReceiptReq.getBar_receipt_id()).get(0);
            receipt = FillReceipt(r.getUniqueReceiptID());
        }

        return new ResponseEntity<>(receipt, HttpStatus.OK);
    }

    private ReceiptResponse FillReceipt(Integer uniqueReceiptID) {
        ReceiptResponse result = new ReceiptResponse();

        List<String> names = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        List<ReceiptProductsDB> products = receiptProductsDBRepository.findByUniqueReceiptID(uniqueReceiptID);

        for (ReceiptProductsDB product : products) {
            if (product.getIsPaidFlag() != FLAG_PAID) {
                ids.add(product.getProductID());
                BarProductsDB pr = barProductsDBRepository.findByProductID(product.getProductID()).get(0);
                names.add(pr.getProductName());
            }
        }

        result.setProducts_id(ids);
        result.setProducts_names(names);
        result.setStatus(STATUS_OK);
        result.setUnique_receipt_id(uniqueReceiptID);

        return result;
    }

    @RequestMapping("/payment_req")
    public ResponseEntity<StatusResponse> PaymentRequest(@RequestBody PaymentRequest paymentRequest) {
        StatusResponse statusResp = new StatusResponse();

        if (userInfoDBRepository.findBySessionToken(paymentRequest.getSession_token()).isEmpty()) {
            statusResp.setStatus(STATUS_SESSION_NOT_EXISTS);
        }
        else if (receiptInfoDBRepository.findByUniqueReceiptID(paymentRequest.getUnique_receipt_id()).isEmpty()) {
            statusResp.setStatus(STATUS_RECIPT_NOT_EXISTS);
        }
        else {
            HandlePayment(paymentRequest.getProducts_id(), paymentRequest.getUnique_receipt_id());
            statusResp.setStatus(STATUS_OK);
        }

        return new ResponseEntity<>(statusResp, HttpStatus.OK);
    }

    private void HandlePayment(List<Integer> productsIDs, Integer uniqueReceiptID) {
        for (Integer prodID : productsIDs) {
            ReceiptProductsDB product = receiptProductsDBRepository.findByUniqueReceiptIDAndProductID(uniqueReceiptID, prodID).get(0);
            product.setIsPaidFlag(FLAG_PAID);
            receiptProductsDBRepository.save(product);
        }
    }


    @RequestMapping("/init")
    public void Init() {

        if (userInfoDBRepository.findByRoleID(ROLE_ADMIN).isEmpty()) {
            UserInfoDB admin = new UserInfoDB();
            admin.setEmail(MAIL_FROM);
            admin.setRoleID(ROLE_ADMIN);
            admin.setUserToken(GenerateUserToken());
            admin.setPasswordHash(CalculateSHA256(EASY_PASS));

            userInfoDBRepository.save(admin);
        }

        if (userInfoDBRepository.findByRoleID(ROLE_USER).isEmpty()) {
            UserInfoDB user = new UserInfoDB();
            user.setEmail("volodya.vladimir.srg@gmail.com");
            user.setRoleID(ROLE_USER);
            user.setUserToken(GenerateUserToken());
            user.setPasswordHash(CalculateSHA256("qwe"));

            userInfoDBRepository.save(user);
        }
    }


    private String CalculateSHA256(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            return FromBytesToHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String GenerateRandomString(Integer length) {
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        while (length-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("vldmr.rr3@gmail.com");
        mailSender.setPassword("vovkovalev1");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.debug", "true");

        return mailSender;
    }

    public void SendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    private String GenerateOTP(int length) {
        int i;
        String result = "";
        Random rd = new Random();

        for (i = 0; i < length; i++) {
            result += Integer.toString(rd.nextInt(10));
        }

        return result;
    }

    private String GenerateUserToken() {
        Random rd = new Random();
        byte[] randomBytes = new byte[32];
        rd.nextBytes(randomBytes);

        return FromBytesToHexString(randomBytes);
    }

    private static String FromBytesToHexString(byte[] bytesArray) {
        String buff = "";
        for (byte b : bytesArray)
            buff += String.format("%02X", b);

        return buff;
    }


    public static void main(String[] args) {

    }
}



