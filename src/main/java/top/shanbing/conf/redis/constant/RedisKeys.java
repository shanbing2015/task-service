package top.shanbing.conf.redis.constant;

/**
 *
 * 这里统一管理Redis的Key
 * @author KangKai
 * @date 2017/3/31.
 */
public interface RedisKeys {

    /*
    * 福袋后台配置大额现金领取人数
    * */
    String LUCKYBAG_CONFIG_LUCKYBAG_AMOUNT_OPEN = "luckybag:cash:amount:open";

    /*
    * 福袋后台配置大额现金领取人数
    * */
    String LUCKYBAG_CONFIG_LUCKYBAG_AMOUNT_OPENED = "luckybag:cash:amount:opened";

    /*
    * 福袋后台配置大额现金领取人数
    * */
    String LUCKYBAG_CONFIG_LUCKYBAG_BIG_OPENED = "luckybag:big:opened:count";

    /*
    * 福袋后台配置倒粉人数
    * */
    String LUCKYBAG_CONFIG_LUCKYBAG_ADD_USER = "luckybag:serviceno:add:user";

    /*
    * 福袋后台配置当前已使用金额
    * */
    String LUCKYBAG_CONFIG_LUCKYBAG_CASH_AMOUNT = "luckybag:cash:amount:opened";

    /*
    * 福袋后台配置活动参与人数
    * */
    String LUCKYBAG_CONFIG_LUCKYBAG_UV = "luckybag:uv";

    /*
    * 福袋后台配置更新channel
    * */
    String LUCKYBAG_CONFIG_CHANNEL = "luckybag:config";

    /*
    *
    * 福袋后台配置
    *
    * */
    String LUCKYBAG_COMMON_CONFIG_KEY = "luckybag:config:common";
    /**
     * 用于测试的redisKey
     */
    //String TEST_REDIS_KEY = "api:test:%s:%s";

    String TEST_REDIS_KEY = "api:test:%s";


    /**
     * 开团成功,且需要模拟成团的队列key
     */
    String AUTO_GROUPON_KEY = "task:groupon:simulate_list";

    /**
     * 用户购物车key shop_cart_item_list_key:u_id
     */
    String USER_CART_KEY = "shop_cart_item_list_key:%d";

    /**
     * 商品skuBo信息key api:sku:info:sku_id
     */
    String SKU_INFO_KEY = "api:sku:base:info:%d";

    /**
     * 商品Bo信息key api:sku:info:item_id
     */
    String PRODUCT_BASE_INFO_KEY = "api:product:base:info:%d";


    /**
     * 1688商品列表key
     * api:1688:product:%s
     * %s = 对象toString md5
     */
    String CLOUD_PRODUCT_LIST = "api:cloud:product:%s";

    /**
     * 商品父品类key
     * api:backend:item:parent:class:%s
     */
    String ITEM_PARENT_CLASS = "api:backend:item:parent:class:%s";

    /**
     * 品牌基本信息key
     * api:backend:brand:%d
     */
    String BRAND_INFO = "api:backend:item:parent:class:%s";

    /**
     * api:backend:xdp:accesstoken:%d
     */
    String XDP_ACCESS_TOKEN = "api:backend:xdp:accesstoken:%d";

    /**
     * see:queen:official:account:accesstoken
     */
    String SEE_OFFICIAL_ACCOUNT_ACCESS_TOKEN = "see:official:account:accesstoken:%s";

    /**
     * See服务号公众号关注用户列表
     */
    String GONGZHONGHAO_USER_SUBSCRIBE_LIST = "api:backend:gongzhonghao:userlist";

    /**
     * 因为福袋项目关注公众号的用户列表
     */
    String LUCKYBAG_SUBSCRIBE_USER_LIST = "api:backend:luckybag:subscribe:userlist";

    /**
     * 福袋项目图片的mediaId
     */
    String LUCKYBAG_IMG_MEDIA_ID = "api:backend:official:media:Id:%s";

    /**
     * 公众号openId和userId的映射
     */
    String OFFICIAL_ACCOUNT_OPENID = "api:official:account:openid:map";
    
    /**
     * xdp:decorate:module:target:小店铺id:模块id
     */
    //String XDP_DECORATE_MODULE_TARGET_BYID="decorate:target:id:%d:%d";
    
    /**
     * xdp:decorate:module:target:小店铺id:模块type
     */
    String XDP_DECORATE_MODULE_TARGET_BYTYPE="decorate:target:type:%d:%d";
    
    String XDP_DECORATE_CHANGE="decorate:change";
    
    /**
     * 保存小店铺的最新编辑时间 hash key为小店铺id
     */
    String XDP_DECORATE_LAST_EDIT_TIME="decorate:edit:time";
    /**
     * 保存小店铺的最新发布时间 key为小店铺id
     */
    String XDP_DECORATE_LAST_PUBLISH_TIME="decorate:publish:time";
    
    /**
     * 保存小店铺是否第一次初始化了
     */
    String XDP_DECORATE_INIT_FLAG="decorate:init:flag";

    /**
     * 缓存小店铺的模块信息   hash  key为xdpid value所有信息
     */
    String XDP_MODULE_RELEASE="xdp_module_release";


    /**
     * 商品售罄状态字段
     */
    String ITEM_ON_SALE_STATUS = "api:backend:onsale:%d";


    /**
     * 抽奖福袋公众号unionId与openId的映射
     */
    String LUCKY_DRAW_OFFICIAL_ACCOUNT_UNIONID = "api:luckydraw:official:account:unionid:map";
    /**
     * 抽奖福袋公众号openId与unionid的映射
     */
    String LUCKY_DRAW_OFFICIAL_ACCOUNT_OPENID = "api:luckydraw:official:account:openid:map";
    /**
     * 抽奖福袋活动缓存
     */
    String LUCKY_DRAW_CURRENT_PHASE_ACTIVITY = "lucky:draw:current:act";
    String LUCKY_DRAW_NEXT_PHASE_ACTIVITY = "lucky:draw:next:act";
    String LUCKY_DRAW_EXPIRED_ACTIVITY = "lucky:draw:expired:act";
    String LUCKY_DRAW_ACTIVITY_INFO = "lucky:draw:act:%s";

    //抽奖福袋小程序客服会话缓存
    String LUCKY_DRAW_CUSTOM_MSG_SESSION = "lucky:draw:xcx:msg:session:%s";
    //抽奖福袋小程序客服会话是否发送过图文消息缓存
    String LUCKY_DRAW_CUSTOM_MSG_SEND_FLAG = "lucky:draw:xcx:msg:flag:%s";
    /**
     * 抽奖福袋小程序openid与formId的映射
     */
    String LUCKY_DRAW_WEAPP_OPENID_FORMID = "api:luckydraw:weapp:openid:formid:map";

    /**
     * 锁拼团模板KEY
     */
    String LOCK_GROUPON_ACTIVITY_TEMPLATE = "api:backend:lock:groupon:template:%d:%d";

    String GROUPON_TASK_RESULT = "api:backend:groupon:task:result:%s";

    String GROUPON_TASK_RESULT_PERCENTAGE = "api:backend:groupon:task:result:percentage:%s";


    String BATCH_COPY_RESULT="h:batchcopy:result";

    /**
     *  中心化邮件redis key
     */
    String KEY_PLATFORM_EMAIL = "key:platform:email";

    String LOCK_PLATFORM_BACKEND_WITHDRAW = "lock:platform:backend:withdraw";

    String PLATFORM_WITHDRAWAL_ROLLBACK = "platform:withdrawal:rollback:%s";
}
