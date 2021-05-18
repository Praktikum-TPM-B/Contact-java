package com.maulanakurnia.contact.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.maulanakurnia.contact.R;
import com.maulanakurnia.contact.data.model.Contact;
import com.maulanakurnia.contact.ui.view.FormActivity;
import com.maulanakurnia.contact.ui.viewmodel.ContactViewModel;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Maulana Kurnia on 5/18/2021
 * Keep Coding & Stay Awesome!
 **/
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<Contact> contactList;
    private Context context;

    public static final String CONTACT_ID       = "contact_id";

    public ContactAdapter(List<Contact> contactList, Context context) {
        this.contactList            = contactList;
        this.context                = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact,parent,false));
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        final BottomSheetDialog profileDialog = new BottomSheetDialog(context,
                R.style.BottomSheetDialogTheme);
        View view= LayoutInflater.from(context).inflate(R.layout.bottom_sheet_profile,null);

        // Retrieve data from one position directly, where "positon" is where the focus is
        Contact contact = contactList.get(position);

        String initials = "";
        String[] name;
        if(contact.getName().split(" ").length >= 2) {
            name   = Arrays.copyOfRange(contact.getName().split(" "), 0, 2);
        }else {
            name   = Arrays.copyOfRange(contact.getName().split(" "), 0, 1);
        }

        for (String s : name ) {
            initials+=s.charAt(0);
        }

        String finalInitials = initials;
        holder.cardView.setOnClickListener(v -> {
            TextView pd_name        = view.findViewById(R.id.bs_profile_name);
            TextView pd_mobile      = view.findViewById(R.id.bs_profile_mobile);
            TextView pd_address     = view.findViewById(R.id.bs_profile_address);
            TextView pd_gender      = view.findViewById(R.id.bs_profile_gender);
            Button btnEdit          = view.findViewById(R.id.bs_profile_edit_button);
            Button  btnDelete       = view.findViewById(R.id.bs_profile_delete_button);
            TextView nameImage      = view.findViewById(R.id.bs_profile_name_image);

            pd_name.setText(contact.getName());
            pd_mobile.setText(contact.getMobile());
            pd_address.setText(contact.getAddress());
            pd_gender.setText(contact.getGender());
            nameImage.setText(finalInitials);

            btnEdit.setOnClickListener(v1 -> {
                Intent intent = new Intent(context, FormActivity.class);
                intent.putExtra(CONTACT_ID, contact.getId());
                profileDialog.dismiss();
                context.startActivity(intent);
            });

            btnDelete.setOnClickListener(v2 -> {
                profileDialog.dismiss();
                @SuppressLint("ResourceType")
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.layout.dialog_question);
                View dialog = LayoutInflater.from(context).inflate(
                        R.layout.dialog_question,
                        view.findViewById(R.id.layout_dialog)
                );

                String text = "Do you really want to permanently delete "+ contact.getName() +"? Data will not be recovered!";
                SpannableString ss = new SpannableString(text);

                TextView desc               = dialog.findViewById(R.id.dialog_description);
                StyleSpan boldSpan          = new StyleSpan(Typeface.BOLD);
                UnderlineSpan underlineSpan = new UnderlineSpan();
                int lengthName              = 41+contact.getName().length();

                ss.setSpan(boldSpan, 41, lengthName, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(underlineSpan, 41, lengthName, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                desc.setText(ss);

                builder.setView(dialog);

                final AlertDialog alertDialog = builder.create();

                dialog.findViewById(R.id.dialog_action_cancel).setOnClickListener(v3 -> {
                    alertDialog.dismiss();
                    profileDialog.show();
                });

                dialog.findViewById(R.id.dialog_action_delete).setOnClickListener(v4 -> {
                    ContactViewModel.deleteById(contact.getId());
                    alertDialog.dismiss();
                    profileDialog.dismiss();
                    Toast.makeText(context, "Successful delete "+pd_name.getText().toString(), Toast.LENGTH_SHORT).show();
                });

                if(alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }

                alertDialog.show();
            });

            profileDialog.setContentView(view);
            profileDialog.show();
        });

        // knowing the contact elements from the adapter,
        // we can bind/set them view items
        holder.name.setText(contact.getName());
        holder.mobile.setText(contact.getMobile());
        holder.nameImage.setText(initials);
    }

    @Override
    public int getItemCount() {
        // this method is implemented so the recyclerview
        // knows exactly how much data is getting,
        // this improving peformance of the application
        // to do so, we need to know the size of our data.
        // In our case, it's just a LiveData container of Contacts.
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name, mobile, nameImage;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            name        = itemView.findViewById(R.id.contact_name);
            mobile      = itemView.findViewById(R.id.contact_mobile);
            nameImage   = itemView.findViewById(R.id.contact_img_name);
            cardView    = itemView.findViewById(R.id.card_contact);
        }

    }
}
