import tensorflow as tf
from tensorflow import keras
import numpy as np
from image_processing import preprocess_image, num_to_char, max_len


class Recognizer:

    def __init__(self, tf_model_dir : str) -> None:
        model = keras.models.load_model(tf_model_dir)
        self.prediction_model = keras.models.Model(
            model.get_layer(name="image").input, model.get_layer(name="dense2").output
        )

    def __decode_batch_predictions(self, pred):
        """
        A utility function to decode the output of the network.
        """
        input_len = np.ones(pred.shape[0]) * pred.shape[1]
        results = keras.backend.ctc_decode(pred, input_length=input_len, greedy=True)[0][0][:, :max_len]
        output_text = []
        for res in results:
            res = tf.gather(res, tf.where(tf.math.not_equal(res, -1)))
            res = tf.strings.reduce_join(num_to_char(res)).numpy().decode("utf-8")
            output_text.append(res)
        return output_text


    def recognize_from_path(self, img_path):
        """
        Recognize text from image in img_path
        """
        preprocessed_img = preprocess_image(img_path)
        prediction = self.prediction_model.predict({'image': np.array([preprocessed_img])})
        return self.__decode_batch_predictions(prediction)[0]

