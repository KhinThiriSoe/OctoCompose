/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.octocompose.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.unaryPlus
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.ui.core.*
import androidx.ui.foundation.DrawImage
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.res.imageResource
import androidx.ui.tooling.preview.Preview
import com.raywenderlich.android.octocompose.R
import com.raywenderlich.android.octocompose.model.Member
import com.raywenderlich.android.octocompose.model.PreviewMember

class TeamMembersActivity : AppCompatActivity() {

  private val membersState = MembersState()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val viewModel = ViewModelProviders.of(this)[TeamMembersViewModel::class.java]
    viewModel.getMembers("raywenderlich").observe(this, Observer<List<Member>> { members ->
      membersState.members = members
    })

    setContent {
      TeamMembersScreen {
        TeamMembersContent(membersState)
      }
    }
  }
}

@Composable
fun TeamMembersScreen(children: @Composable() () -> Unit) {
  MaterialTheme {
    children()
  }
}

@Composable
fun TeamMembersContent(membersState: MembersState = MembersState()) {
  FlexColumn {
    inflexible {
      TopAppBar(title = { Text("OctoCompose") }
      )
    }
    expanded(1f) {
      TeamMembers(membersState)
    }
  }
}

@Composable
fun TeamMembers(membersState: MembersState) {
  if (membersState.members.isNotEmpty()) {
    VerticalScroller {
      Column {
        membersState.members.forEach { member ->
          TeamMember(member = member)
          MemberDivider()
        }
      }
    }
  }
}

@Model
class MembersState(var members: List<Member> = listOf())


@Composable
fun TeamMember(member: Member) {
  val image = +image(member.avatarUrl) ?: +imageResource(R.drawable.placeholder)

  FlexRow(
    crossAxisAlignment = CrossAxisAlignment.Center,
    modifier = Spacing(8.dp)
  ) {
    inflexible {
      Container(width = 80.dp, height = 80.dp, modifier = Spacing(8.dp)) {
        Clip(shape = RoundedCornerShape(8.dp)) {
          DrawImage(image = image)
        }
      }
    }
    expanded(1f) {
      Text(text = member.login)
    }
    inflexible {
      Text(text = member.type)
    }
  }
}

@Composable
private fun MemberDivider() {
  Opacity(opacity = 0.08f) {
    Divider()
  }
}