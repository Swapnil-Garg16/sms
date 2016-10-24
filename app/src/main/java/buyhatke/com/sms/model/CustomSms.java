package buyhatke.com.sms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Divya on 5/6/2016.
 */
public class CustomSms implements Parcelable {

    public String address;
    public String date;
    public String body;


    public CustomSms(String address,String date,String body)
    {
        this.address = address;
        this.date = date;
        this.body = body;
    }

    public int describeContents() {
        return this.hashCode();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(date);
        dest.writeString(body);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public CustomSms createFromParcel(Parcel in) {
            return new CustomSms(in);
        }

        public CustomSms[] newArray(int size) {
            return new CustomSms[size];
        }
    };

    public CustomSms(Parcel source) {
        address = source.readString();
        date = source.readString();
        body = source.readString();
    }
}
