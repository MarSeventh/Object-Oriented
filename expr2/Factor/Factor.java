package Factor;

public interface Factor {

    // 求导方法
    Factor derive();

    // 深克隆方法（思考：为什么需要深克隆？）
    Factor clone();
}
