package com.example.alex.fitofan.utils.db;

import com.example.alex.fitofan.models.TrainingModel;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class TrainingDAO extends BaseDaoImpl<TrainingModel, Integer> {

    protected TrainingDAO(ConnectionSource connectionSource,
                          Class<TrainingModel> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<TrainingModel> getAllTrainig() throws SQLException {
        return this.queryForAll();
    }
}
