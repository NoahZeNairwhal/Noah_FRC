package frc.robot.Robot.Drive;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MotorUpdater {
    public MotorController myMotorController;
    public Motors myMotors;
    public boolean tuning = false;
    public double[] last = new double[]{0.0, 0.0};
    public String filePath = "";

    public MotorUpdater(MotorController aController, Motors aMotors) {
        myMotorController = aController;
        myMotors = aMotors;
    }
    public MotorUpdater(MotorController aController, Motors aMotors, boolean tuning) {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        inst.startClient4("team3692-frc2024");
        inst.setServerTeam(3692, NetworkTableInstance.kDefaultPort4);
        SmartDashboard.setNetworkTableInstance(inst);
        
        SmartDashboard.putBoolean("Feedback Ready", false);
        SmartDashboard.putBoolean("Feedback", false);

        myMotorController = aController;
        myMotors = aMotors;
        this.tuning = tuning;
        filePath = "src\\main\\configs\\YouForgotAPathDriveConfig.txt";
    }
    //DO NOT put TuneDriveConfig.txt as the file path. Do something like NoahDriveConfig.txt instead
    public MotorUpdater(MotorController aController, Motors aMotors, boolean tuning, String filePath) {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        inst.startClient4("team3692-frc2024");
        inst.setServerTeam(3692, NetworkTableInstance.kDefaultPort4);
        SmartDashboard.setNetworkTableInstance(inst);

        SmartDashboard.putBoolean("Feedback Ready", false);
        SmartDashboard.putBoolean("Feedback", false);

        myMotorController = aController;
        myMotors = aMotors;
        this.tuning = tuning;
        this.filePath = filePath;
    }

    public void update() {
        last = myMotorController.update(last);
        myMotors.update(last[0], last[1]);

        if(tuning && SmartDashboard.getBoolean("Feedback Ready", false)) {
            myMotorController.tune(SmartDashboard.getBoolean("Feedback", false));

            SmartDashboard.putBoolean("Feedback Ready", false);
            SmartDashboard.putBoolean("Feedback", false);
        }
    }

    public void close() {
        if(tuning) {
            String pidContents = myMotorController.speedAugment.getP() + "\n" + myMotorController.speedAugment.getI() + "\n" +
            myMotorController.speedAugment.getD() + "\n" + myMotorController.turnAugment.getP() + "\n" + 
            myMotorController.turnAugment.getI() + "\n" + myMotorController.turnAugment.getD();

            SmartDashboard.putString("File Write Contents", pidContents);
            SmartDashboard.putString("File Write Path", filePath);
        }

        myMotorController.close();
        myMotors.close();
    }
}
