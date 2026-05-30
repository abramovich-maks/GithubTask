package com.githubtask;

import java.util.List;

record Repository(
        String name,
        String ownerLogin,
        List<Branch> branches
) {
}