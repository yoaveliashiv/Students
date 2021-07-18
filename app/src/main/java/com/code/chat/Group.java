package com.code.chat;

public class Group {

// <ListView
//    android:id="@+id/list_view"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_marginTop="140dp"
//    android:visibility="gone"
//    app:layout_constraintTop_toBottomOf="@+id/button_new_link">
//
//
//
//    </ListView>

        private String name="";
        private String key="";
        private String uidNewGroupSend="";

        public Group() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUidNewGroupSend() {
            return uidNewGroupSend;
        }

        public void setUidNewGroupSend(String uidNewGroupSend) {
            this.uidNewGroupSend = uidNewGroupSend;
        }
    }
