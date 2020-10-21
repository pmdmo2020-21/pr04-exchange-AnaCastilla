package es.iessaladillo.pedrojoya.exchange

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.doAfterTextChanged
import es.iessaladillo.pedrojoya.exchange.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
    }

    private fun setupViews() {
        binding.rdbFromDollar.isEnabled = false
        binding.rdbToEuro.isEnabled = false
        binding.btnExchange.setOnClickListener { btnExchangeOnClick() }
        binding.rdgFrom.setOnCheckedChangeListener { currency, id -> checkedCurrencies(currency, id) }
        binding.rdgTo.setOnCheckedChangeListener { currency, id -> checkedCurrencies(currency, id) }
    }

    private fun changeIconCurrency(imgIcon: ImageView, currency: Currency) {
        imgIcon.setImageResource(currency.drawableResId)
    }

    private fun enableCurrency(dollar: Boolean, euro: Boolean, pound: Boolean, currency: RadioGroup) {
        if (currency != binding.rdgFrom) {
            binding.rdbFromDollar.isEnabled = dollar
            binding.rdbFromEuro.isEnabled = euro
            binding.rdbFromPound.isEnabled = pound
        } else {
            binding.rdbToDollar.isEnabled = dollar
            binding.rdbToEuro.isEnabled = euro
            binding.rdbToPound.isEnabled = pound
        }
    }

    private fun checkedCurrencies(checkedCurrency: RadioGroup, checkedId: Int) {
        when(checkedId) {
            binding.rdbFromDollar.id -> {
                changeIconCurrency(binding.imgFrom, Currency.DOLLAR)
                enableCurrency(false, true, true, currency = checkedCurrency)
            }
            binding.rdbFromEuro.id -> {
                changeIconCurrency(binding.imgFrom, Currency.EURO)
                enableCurrency(true, false, true, currency = checkedCurrency)
            }
            binding.rdbFromPound.id -> {
                changeIconCurrency(binding.imgFrom, Currency.POUND)
                enableCurrency(true, true, false, currency = checkedCurrency)
            }
            binding.rdbToDollar.id -> {
                changeIconCurrency(binding.imgTo, Currency.DOLLAR)
                enableCurrency(false, true, true, currency = checkedCurrency)
            }
            binding.rdbToEuro.id -> {
                changeIconCurrency(binding.imgTo, Currency.EURO)
                enableCurrency(true, false, true, currency = checkedCurrency)
            }
            binding.rdbToPound.id -> {
                changeIconCurrency(binding.imgTo, Currency.POUND)
                enableCurrency(true, true, false, currency = checkedCurrency)
            }
        }
    }

    private fun validateAmount(amount: Double) : Boolean {
       return if (!amount.equals(0)) {
           true
        } else {
           binding.edtAmount.setText(R.string.amount)
           false
       }
    }

    private fun btnExchangeOnClick() {
        val amount = binding.edtAmount.text.toString().toDouble()
        if (validateAmount(amount)) {
            SoftInputUtils.hideSoftKeyboard(binding.edtAmount)

        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun exchange(currency: Double, result: Double, currencyFrom: Currency, currencyTo: Currency ) {
        Toast.makeText(this, getString(R.string.result, currency, currencyFrom, result, currencyTo), Toast.LENGTH_SHORT).show();
    }



}

