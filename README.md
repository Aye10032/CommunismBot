# FSBot
A new QQ bot based on mirai

文件目录：
    bot: 和qq交互相关的放这里
        func: bot的方法
        task: 时间类型任务
    foundation: 基础层 一般是exception entity 之类的
        exception: 异常类
        entity: 实体类
            dto: 传输层类 一般是request之类的
            vo: 展示层类
            do: 存储层类
        util: 工具类
        assembler: 组装类
    controller: controller放这里
    service: service 一般是bot和web通用的服务提取
    mapper: mapper层
    
    