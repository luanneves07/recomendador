package recomendador;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class RecomendaProduto {
	private static final Logger LOG = Logger.getLogger(RecomendaProduto.class);
	
	public static void main(String[] args) {
		/*Arquivo qu contém as informações correspondenes a cada usuário*/
		File dataSrc = new File("./src/main/resources/dados.csv");
		DataModel model = null;
		UserSimilarity similarity = null;
		try {
			/*<odelo baseado no arquivo utilizado*/
			model = new FileDataModel(dataSrc);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		try {
			/*Utilizado para criar as funções de similaridade (utilizado o padrão de correlação de simlaridade)*/
			similarity = new PearsonCorrelationSimilarity(model);
		} catch (TasteException e) {
			LOG.error(e.getMessage(), e);
		}
		/*Calcula a proximidade (quem é vizinho de quem)*/
		ThresholdUserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
		/*Recomendador*/
		GenericUserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
		try {
			/*Recomendações*/
			List<RecommendedItem> recommendations = recommender.recommend(2, 3);
			for(RecommendedItem recommendation : recommendations) {
				System.out.println(recommendation);
			}
		} catch (TasteException e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
