import java.util.Arrays;
import java.util.List;

/**
 * Created by neelshah on 10/31/15.
 */
public class WeightedQueen extends ConsensusAlgorithm {

    public WeightedQueen(int i, int n, Value V, MsgHandler msg, MessageType valueType, List<Double> weights, boolean actFaulty) {
        super(i, n, V, msg, valueType, weights, actFaulty);
        rho = 1.0/4;
    }

    @Override
     public void runPhaseOne(){
        double s0 = 0.0, s1 = 0.0;
        resetValues();

        // Phase One
        if (weights.get(i) > 0) {
            broadcast(V);
        }

        waitForValues();

        for (int j = 0; j < weights.size(); j++) {
            if (weights.get(j) > 0) {
                if (values[j] == Value.TRUE) {
                    s1 += weights.get(j);
                } else {
                    s0 += weights.get(j);
                }
            }
        }

        if (s1 > 0.5) {
            V = Value.TRUE;
            myWeight = s1;
        } else {
            V = Value.FALSE;
            myWeight = s0;
        }

        if(countNullValues() > 0) {
            MsgHandler.debug("Node " + i  + " has null values " + Arrays.toString(values));
        }

    }

    @Override
    public void runLeaderPhase(int q) {
        // Phase Two
        if (q == i) {
            broadcast(V);
        } else {
            waitForValues();

            leaderValue = receiveLeaderValue(q);

            if (myWeight <= 0.75) {
                V = leaderValue;
            }
        }
    }

    @Override
    public void runFaultyNodePhase(int round) {
        super.runFaultyNodePhase(round, true);
    }
}