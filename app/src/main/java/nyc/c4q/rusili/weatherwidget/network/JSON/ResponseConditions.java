package nyc.c4q.rusili.weatherwidget.network.JSON;

public class ResponseConditions {
    conditions conditions;

    public ResponseConditions.conditions getConditions () {
        return conditions;
    }

    public class conditions {
        Current_Observation current_observation;

        public Current_Observation getCurrent_observation () {
            return current_observation;
        }
    }
}
