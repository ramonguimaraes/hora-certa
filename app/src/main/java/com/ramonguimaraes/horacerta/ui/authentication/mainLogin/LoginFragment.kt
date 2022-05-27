package com.ramonguimaraes.horacerta.ui.authentication.mainLogin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var mBinding: FragmentLoginBinding
    private lateinit var activityLaucher: ActivityResultLauncher<Intent>
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentLoginBinding.inflate(inflater)

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(KEY)
            .requestEmail()
            .build()

        activity?.let { googleSignInClient = GoogleSignIn.getClient(it, options) }

        registrationForResultGoogleSignIn()
        configButtonsOnClick()

        return mBinding.root
    }

    private fun registrationForResultGoogleSignIn() {
        activityLaucher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val conta = task.result
                        conta.idToken?.let { googleLogin(it) }
                    } catch (e: Exception) {
                        Log.e("erro", e.message.toString())
                    }
                }
            }
    }

    private fun signIn(googleSignInClient: GoogleSignInClient) {
        val intent = googleSignInClient.signInIntent

        activityLaucher.launch(intent)
    }

    private fun signOut(googleSignInClient: GoogleSignInClient) {
        FirebaseAuth.getInstance().signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            if (it.isSuccessful) {
                activity?.finish()
            }
        }
    }

    private fun googleLogin(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val isNewUser = it.result.additionalUserInfo?.isNewUser
                if (isNewUser == true) {
                    findNavController().navigate(R.id.action_loginFragment_to_clientProfileFragment)
                } else {
                    // Se usuario ja existe, então vai para a tela principal de usuario.
                    Toast.makeText(context, "USUARIO QUE JÁ EXISTE", Toast.LENGTH_SHORT).show()
                }
            } else {
                signOut(googleSignInClient)
                Toast.makeText(context, "Deu errado", Toast.LENGTH_SHORT).show()
                Log.d("error", it.exception.toString())
            }
        }
    }

    private fun configButtonsOnClick() {
        mBinding.buttonGoogleAuth.setOnClickListener {
            signIn(googleSignInClient)
        }

        mBinding.textViewCompanyRegistration.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    companion object {
        private const val KEY =
            "560084543445-sjcsmqjhamao63o4l7jiprbv6k49ddi8.apps.googleusercontent.com"
    }
}
