package com.ud.iot.spring.sequences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.ud.iot.documents.Sequence;

@Component
public class SequenceGeneratorService {

	@Autowired private MongoOperations mongo;

	public Long generateSequence(String sequenceId) {
		final Sequence sequence = mongo.findAndModify(Query.query(Criteria.where("_id").is(sequenceId)), new Update().inc("value", 1),
				FindAndModifyOptions.options().returnNew(true).upsert(true), Sequence.class);
		return sequence.getValue();
	}
}
