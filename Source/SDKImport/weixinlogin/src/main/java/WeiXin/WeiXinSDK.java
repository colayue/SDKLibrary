package WeiXin;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unity3d.player.UnityPlayer;

import org.json.JSONObject;
import java.io.IOException;

import sdkInterface.*;

public class WeiXinSDK extends SDKBase implements ILogin,IPay,IOther
{
    public static  String AppID = "";
    public static  String AppSecret ="";

    public static IWXAPI api;

    @Override
    public void Init(JSONObject json) {

        try {
            AppID = GetProperties().getProperty("AppID");
            AppSecret = GetProperties().getProperty("AppSecret");

            api = WXAPIFactory.createWXAPI(UnityPlayer.currentActivity, WeiXinSDK.AppID);
            api.registerApp(AppID);

            SdkInterface.SendLog("WeiXinSDK Init: AppID " + AppID + " AppSecret " + AppSecret);

        } catch (IOException e)
        {
            SdkInterface.SendError("WeiXinSDK Init Error" + e.toString(),e);
        }
    }

    @Override
    public void Login(JSONObject json)
    {
        SdkInterface.SendLog("WeiXinSDK Login 1 AppID " + AppID + " AppSecret " + AppSecret);

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);

        SdkInterface.SendLog("WeiXinSDK Login 3 ");
    }

    @Override
    public void Pay(JSONObject json)  {
        SendLog("WX Pay " + json.toString());
        String prepayid = null;

        try {
            prepayid = json.getString(SDKInterfaceDefine.Pay_ParameterName_GoodsID);

            PayReq request = new PayReq();
            request.appId = WeiXinSDK.AppID;
            request.partnerId = "1526756671"; //商户号
            request.prepayId= prepayid; //交易会话ID
            request.packageValue = "Sign=WXPay";
            request.nonceStr= "111"; //随机字符串
            request.timeStamp= "222";//北京时间
            request.sign= "333";
            api.sendReq(request);

        } catch (Exception e) {
            SendError("WX Pay Error " + e.toString(),e);
        }
    }

    @Override
    public void Other(JSONObject json) {

    }

    @Override
    public String[] GetFunctionName() {
        return new String[]{};
    }
}
