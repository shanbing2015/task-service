package top.shanbing.util;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import top.shanbing.result.PageResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体转换工具 用于bo->entity,entity->vo boList->entityList,entityList->voList
 *
 * @author KangKai
 * @date 2017年3月28日
 */
public final class EntityConvertUtil {
    private static final Logger LOGGER = Logger.getLogger(EntityConvertUtil.class);
    @SuppressWarnings("rawtypes")
    private static final Map<Class, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<Class, BeanCopier>();

    /**
     * dto转bo
     *
     * @param dto
     * @param boClazz
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> E dtoToBo(T dto, Class<E> boClazz) {
        E boObject = null;
        try {
            boObject = boClazz.newInstance();
            BeanUtils.copyProperties(dto, boObject);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage() + "DTO To BO Failed！");
        }
        return boObject;
    }

    public static <T, E> E convertObj(T obj, Class<E> convertClazz) {
        E convertObject = null;
        try {
            convertObject = convertClazz.newInstance();
            BeanUtils.copyProperties(obj, convertObject);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage() + "convert Failed！");
        }
        return convertObject;
    }

    /**
     * dtoList 转 boList
     *
     * @param dtoList
     * @param voClazz
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> List<E> dtoListToBOList(List<T> dtoList, Class<E> voClazz) {
        E viewObject = null;
        List<E> boList = new ArrayList<E>();
        try {
            for (T entity : dtoList) {
                viewObject = voClazz.newInstance();
                BeanUtils.copyProperties(entity, viewObject);
                boList.add(viewObject);
            }

        } catch (Exception e) {
            LOGGER.warn(e.getMessage() + "dtoList To boList Failed！");
        }
        return boList;
    }

    /**
     * bo转entity
     *
     * @param entityClazz
     * @param bo
     * @return
     */
    public static <T, E> E boToEntity(T bo, Class<E> entityClazz) {
        E entityObject = null;
        try {
            entityObject = entityClazz.newInstance();
            BeanUtils.copyProperties(bo, entityObject);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage() + "BO To Entity Failed！");
        }
        return entityObject;
    }

    /**
     * boList转entityList
     *
     * @param boList
     * @param entityClazz
     * @return
     */
    public static <T, E> List<E> boListToEntityList(List<T> boList, Class<E> entityClazz) {
        E entityObject = null;
        List<E> entityList = new ArrayList<E>();
        try {
            for (T bisObject : boList) {
                entityObject = entityClazz.newInstance();
                BeanUtils.copyProperties(bisObject, entityObject);
                entityList.add(entityObject);
            }

        } catch (Exception e) {
            LOGGER.warn(e.getMessage() + "BoList To EntityList Failed！");
        }
        return entityList;
    }

    /**
     * entity转vo
     *
     * @param entity
     * @param voClazz
     * @return
     */
    public static <T, E> E entityToBO(T entity, Class<E> voClazz) {
        E viewObject = null;
        try {
            viewObject = voClazz.newInstance();
            BeanUtils.copyProperties(entity, viewObject);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage() + "Entity To VO Failed！");
        }
        return viewObject;
    }

    public static <T, E> List<E> entityListToBOList(List<T> entityList, Class<E> voClazz) {
        E viewObject = null;
        List<E> boList = new ArrayList<E>();
        try {
            for (T entity : entityList) {
                viewObject = voClazz.newInstance();
                BeanUtils.copyProperties(entity, viewObject);
                boList.add(viewObject);
            }

        } catch (Exception e) {
            LOGGER.warn(e.getMessage() + "EntityList To boList Failed！");
        }
        return boList;
    }

    /**
     * bo转vo
     *
     * @param bo
     * @param voClazz
     * @return
     */
    public static <T, E> E boToVo(T bo, Class<E> voClazz) {
        E viewObject = null;
        try {
            viewObject = voClazz.newInstance();
            BeanUtils.copyProperties(bo, viewObject);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage() + "Entity To VO Failed！");
        }
        return viewObject;
    }

    /**
     * boList转entityList
     *
     * @param boList
     * @param entityClazz
     * @return
     */
    public static <T, E> List<E> boListToVoList(List<T> boList, Class<E> entityClazz) {
        E voObject = null;
        List<E> entityList = new ArrayList<E>();
        try {
            for (T bisObject : boList) {
                voObject = entityClazz.newInstance();
                BeanUtils.copyProperties(bisObject, voObject);
                entityList.add(voObject);
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage() + "BoList To voList Failed！");
        }
        return entityList;
    }

    public static <E, T> PageResult<E> boPageResultToVoPageResult(PageResult<T> boPageResult, Class<E> entityClazz) {
        PageResult<E> pageResult = new PageResult<>();
        E voObject = null;
        List<E> entityList = new ArrayList<E>();
        try {
            for (T bisObject : boPageResult.getList()) {
                voObject = entityClazz.newInstance();
                BeanUtils.copyProperties(bisObject, voObject);
                entityList.add(voObject);
            }
            return new PageResult<E>(boPageResult.getCount(), entityList);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage() + "BoList To voList Failed！");
        }
        return pageResult;
    }

    /**
     * entityList转voList
     *
     * @param entityList
     * @param voClazz
     * @return
     */
    public static <T, E> List<E> entityListToVOList(List<T> entityList, Class<E> voClazz) {
        E viewObject = null;
        List<E> voList = new ArrayList<E>();
        try {
            for (T entity : entityList) {
                viewObject = voClazz.newInstance();
                BeanUtils.copyProperties(entity, viewObject);
                voList.add(viewObject);
            }

        } catch (Exception e) {
            LOGGER.warn(e.getMessage() + "EntityList To VoList Failed！");
        }
        return voList;
    }

    public static <T> T conver2Bean(Object from, Class<T> to) {
        T result = null;
        try {
            result = to.newInstance();
            Class<? extends Object> fromClass = from.getClass();
            BeanCopier copier = null;
            if (!BEAN_COPIERS.containsKey(fromClass)) {
                copier = BeanCopier.create(fromClass, to, false);
                BEAN_COPIERS.put(fromClass, copier);
            } else {
                copier = BEAN_COPIERS.get(fromClass);
            }
            copier.copy(from, result, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> List<T> conver2List(List<?> fromList, Class<T> to) {
        if (fromList != null) {
            List<T> toList = new LinkedList<T>();
            for (Object item : fromList) {
                toList.add(conver2Bean(item, to));
            }
            return toList;
        } else {
            return null;
        }
    }

    public static <T> PageResult<T> converPageInfo(PageResult<? extends Object> from, Class<T> to) {
        if (from != null) {
            PageResult<T> result = new PageResult<T>();
            BeanUtils.copyProperties(from, result);
            if (from.getList() != null) {
                List<T> toList = new LinkedList<T>();
                for (Object item : from.getList()) {
                    toList.add(conver2Bean(item, to));
                }
            }
            return result;
        } else {
            return null;
        }
    }

}
