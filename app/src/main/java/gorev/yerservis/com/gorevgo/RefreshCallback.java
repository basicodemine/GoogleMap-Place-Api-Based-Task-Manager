package gorev.yerservis.com.gorevgo;



public class RefreshCallback {

        public interface RefreshListener {
            void stateChanged();
        }

        private static RefreshCallback mInstance;
        private RefreshListener mListener;
        private int mState;

        private RefreshCallback() {}

        public static RefreshCallback getInstance() {
            if(mInstance == null) {
                mInstance = new RefreshCallback();
            }
            return mInstance;
        }

        public void setListener(RefreshListener listener) {
            mListener = listener;
        }

        public void changeState(int change) {
            if(mListener != null) {
                mState = change;
                notifyStateChange();
            }
        }


        private void notifyStateChange() {
            mListener.stateChanged();
        }

}

