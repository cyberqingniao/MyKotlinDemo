package com.yjp.mydemo.entity

/**
 * 视频model$
 * @author yjp
 * @date 2020/7/27 16:40
 */
data class QxEntity(
    //查询数据总数
    var total:Int,
    //当前页码
    var pageNo:Int,
    //每页记录总数
    var pageSize:Int,
    //监控列表
    var list: List<QxBean>
)

data class QxBean(
    //海拔
    var altitude:String,
    //监控点唯一标识
    var cameraIndexCode:String,
    //监控点名称
    var cameraName:String,
    //监控点类型，参考附录A.4
    var cameraType:String,
    //监控点类型说明
    var cameraTypeName:String,
    //设备能力集，参考附录A.22
    var capabilitySet:String,
    //能力集说明
    var capabilitySetName:String,
    //智能分析能力集，扩展字段，暂不使用
    var intelligentSet:String,
    //智能分析能力集说明，扩展字段，暂不使用
    var intelligentSetName:String,
    //通道编号
    var channelNo:String,
    //通道类型，附录A.5
    var channelType:String,
    //通道类型说明
    var channelTypeName:String,
    //创建时间，采用ISO8601标准，如2018-07-26T21:30:08+08:00 表示北京时间2018年7月26日21时30分08秒
    var createTime:String,
    //所属编码设备唯一标识
    var encodeDevIndexCode:String,
    //所属设备类型说明，扩展字段，暂不使用
    var encodeDevResourceType:String,
    //所属设备类型说明，扩展字段，暂不使用
    var encodeDevResourceTypeName:String,
    //监控点国标编号，即外码编号externalIndexCode
    var gbIndexCode:String,
    //安装位置，详见附录附录A.81 安装位置
    var installLocation:String,
    //键盘控制码
    var keyBoardCode:String,
    //纬度
    var latitude:String,
    //经度
    var longitude:String,
    //摄像机像素（1-普通像素，2-130万高清，3-200万高清，4-300万高清），扩展字段，暂不使用
    var pixel:String,
    //云镜类型（1-全方位云台（带转动和变焦），2-只有变焦,不带转动，3-只有转动，不带变焦，4-无云台，无变焦），扩展字段，暂不使用
    var ptz:String,
    //云镜类型说明，扩展字段，暂不使用
    var ptzName:String,
    //云台控制(1-DVR，2-模拟矩阵，3-MU4000，4-NC600)，扩展字段，暂不使用
    var ptzController:String,
    //云台控制说明，扩展字段，暂不使用
    var ptzControllerName:String,
    //录像存储位置
    var recordLocation:String,
    //录像存储位置说明
    var recordLocationName:String,
    //所属区域唯一标识
    var regionIndexCode:String,
    //在线状态（0-未知，1-在线，2-离线），扩展字段，暂不使用
    var status:String,
    //状态说明
    var statusName:String,
    //传输协议，参考附录A.40
    var transType:String,
    //传输协议类型说明
    var transTypeName:String,
    //接入协议，参考附录A.6
    var treatyType:String,
    //接入协议类型说明
    var treatyTypeName:String,
    //可视域相关（JSON格式），扩展字段，暂不使用
    var viewshed:String,
    //更新时间
    //采用ISO8601标准，如2018-07-26T21:30:08+08:00
    //表示北京时间2017年7月26日21时30分08秒
    var updateTime:String

)