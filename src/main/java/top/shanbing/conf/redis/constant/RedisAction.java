package top.shanbing.conf.redis.constant;

/**
 * Redis的动作
 *
 * @author KangKai
 * @date 2017/3/31.
 */
public enum RedisAction {
    /**
     * 先从redis获取，未命中再去业务层获取
     */
    REDIS_FIRST,

    /**
     * 穿透redis到业务层获取，适用于刷新redis的场景
     */
    STAB_REDIS
}
