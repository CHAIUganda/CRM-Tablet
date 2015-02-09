package org.chai.util;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import org.chai.R;

/**
   * Created by victor on 2/9/15.
   */
 public class StockTableRow extends TableRow {
    LayoutParams rowParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    LayoutParams brandParams = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.CENTER);
    LayoutParams stockLevelParams = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.LEFT);
    LayoutParams buyingPriceParams = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.LEFT);
    LayoutParams sellingPriceParams = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, Gravity.LEFT);

    TextView brandLabel;
    EditText stockLevelTxt;
    EditText buyingPriceTxt;
    EditText sellingPriceTxt;


    public StockTableRow(Context context,String brand,String stockLevel,String buyingPrice,String sellingPrice){
        super(context);
        this.setLayoutParams(rowParams);
        this.setWeightSum(1f);
        this.setBackgroundColor(Color.WHITE);

        brandLabel = new TextView(context);
        brandParams.weight = 0.4f;
        brandLabel.setBackgroundResource(R.drawable.bordertextfield);
        brandLabel.setLayoutParams(brandParams);
        brandLabel.setText(brand);

        stockLevelTxt = new EditText(context);
        stockLevelParams.weight = 0.2f;
        stockLevelTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
        stockLevelTxt.setBackgroundResource(R.drawable.bordertextfield);
        stockLevelTxt.setLayoutParams(stockLevelParams);
        stockLevelTxt.setText(stockLevel);

        buyingPriceTxt = new EditText(context);
        buyingPriceParams.weight = 0.2f;
        buyingPriceTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
        buyingPriceTxt.setBackgroundResource(R.drawable.bordertextfield);
        buyingPriceTxt.setLayoutParams(buyingPriceParams);
        buyingPriceTxt.setText(buyingPrice);

        sellingPriceTxt = new EditText(context);
        sellingPriceParams.weight = 0.2f;
        sellingPriceTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
        sellingPriceTxt.setBackgroundResource(R.drawable.bordertextfield);
        sellingPriceTxt.setLayoutParams(sellingPriceParams);
        sellingPriceTxt.setText(sellingPrice);

        this.addView(brandLabel);
        this.addView(stockLevelTxt);
        this.addView(buyingPriceTxt);
        this.addView(sellingPriceTxt);
    }

    public String getBrandName() {
        return brandLabel.getText().toString();
    }

    public void setBrandName(String brandName) {
        this.brandLabel.setText(brandName);
    }

    public String getStockLevel() {
        return stockLevelTxt.getText().toString();
    }

    public void setStockLevel(String stockLevel) {
        this.stockLevelTxt.setText(stockLevel);
    }

    public String getBuyingPrice() {
        return buyingPriceTxt.getText().toString();
    }

    public void setBuyingPrice(String buyingPrice) {
        this.buyingPriceTxt.setText(buyingPrice);
    }

    public String getSellingPrice() {
        return sellingPriceTxt.getText().toString();
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPriceTxt.setText(sellingPrice);
    }


}
