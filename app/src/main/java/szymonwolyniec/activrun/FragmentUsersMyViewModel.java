package szymonwolyniec.activrun;

import android.support.annotation.NonNull;

public class FragmentUsersMyViewModel
{

        private String nickText;
        private String nameAndSurnameText;


        public FragmentUsersMyViewModel(@NonNull final String nickText, @NonNull final String nameAndSurnameText)
        {
            setSimpleText(nickText, nameAndSurnameText);

        }


        @NonNull
        public String getNickText()
        {
            return nickText;
        }

        @NonNull
        public String getNameAndSurnameText()
        {
            return nameAndSurnameText;
        }


        public void setSimpleText(@NonNull final String nickText, @NonNull final String nameAndSurnameText)
        {
            this.nickText = nickText;
            this.nameAndSurnameText = nameAndSurnameText;
        }

}
